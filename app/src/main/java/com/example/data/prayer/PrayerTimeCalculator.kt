package com.example.data.prayer

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

data class PrayerTimesToday(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val fajrMillis: Long,
    val sunriseMillis: Long,
    val dhuhrMillis: Long,
    val asrMillis: Long,
    val maghribMillis: Long,
    val ishaMillis: Long,
    val nextPrayerName: String,
    val nextPrayerTime: String,
    val nextPrayerMillis: Long,
    val countdownMillis: Long,
    val dateGregorianFormatted: String,
    val dateHijriFormatted: String
)

data class CityLocation(
    val nameEn: String,
    val nameBn: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)

object CityData {
    val CITIES = listOf(
        CityLocation("Dhaka", "ঢাকা", "Bangladesh", 23.8103, 90.4125),
        CityLocation("Chittagong", "চট্টগ্রাম", "Bangladesh", 22.3569, 91.7832),
        CityLocation("Sylhet", "সিলেট", "Bangladesh", 24.8949, 91.8687),
        CityLocation("Rajshahi", "রাজশাহী", "Bangladesh", 24.3745, 88.6042),
        CityLocation("Khulna", "খুলনা", "Bangladesh", 22.8456, 89.5403),
        CityLocation("Barisal", "বরিশাল", "Bangladesh", 22.7010, 90.3535),
        CityLocation("Rangpur", "রংপুর", "Bangladesh", 25.7439, 89.2752),
        CityLocation("Mymensingh", "ময়মনসিংহ", "Bangladesh", 24.7471, 90.4203),
        CityLocation("Comilla", "কুমিল্লা", "Bangladesh", 23.4682, 91.1788),
        CityLocation("Bogra", "বগুড়া", "Bangladesh", 24.8465, 89.3777),
        CityLocation("Mecca", "মক্কা", "Saudi Arabia", 21.3891, 39.8579),
        CityLocation("Medina", "মদিনা", "Saudi Arabia", 24.5247, 39.5692),
        CityLocation("Kuala Lumpur", "কুয়ালালামপুর", "Malaysia", 3.1390, 101.6869),
        CityLocation("London", "লন্ডন", "UK", 51.5074, -0.1278),
        CityLocation("New York", "নিউইয়র্ক", "USA", 40.7128, -74.0060),
        CityLocation("Dubai", "দুবাই", "UAE", 25.2048, 55.2708),
        CityLocation("Toronto", "টরন্টো", "Canada", 43.6532, -79.3832)
    )
}

object PrayerTimeCalculator {

    // Calculation Method configs: Fajr angle, Isha angle or fixed minutes
    fun calculate(
        calendar: Calendar = Calendar.getInstance(),
        latitude: Double = 23.8103,
        longitude: Double = 90.4125,
        calculationMethod: String = "KARACHI",
        asrMethod: String = "HANAFI",
        is24Hour: Boolean = false
    ): PrayerTimesToday {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val timezone = calendar.timeZone.getOffset(calendar.timeInMillis).toDouble() / (1000.0 * 3600.0)

        // Julian Date
        val jd = computeJulianDate(year, month, day) - longitude / (15.0 * 24.0)

        // Sun declination and Equation of Time
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(degToRad(g)) + 0.020 * sin(degToRad(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val ra = radToDeg(atan2(cos(degToRad(e)) * sin(degToRad(l)), cos(degToRad(l)))) / 15.0
        val rightAscension = fixHour(ra)

        val declination = radToDeg(asin(sin(degToRad(e)) * sin(degToRad(l))))
        val equationOfTime = (q / 15.0) - rightAscension

        // Solar Noon (Transit)
        val transit = 12.0 + timezone - (longitude / 15.0) - equationOfTime

        // Sunrise & Sunset angle is -0.8333 degrees
        val sunAlt = -0.8333
        val hSun = computeHourAngle(sunAlt, latitude, declination)
        val sunriseHour = transit - hSun / 15.0
        val sunsetHour = transit + hSun / 15.0

        // Fajr angle
        val fajrAngle = when (calculationMethod) {
            "MWL" -> 18.0
            "ISNA" -> 15.0
            "MAKKAH" -> 18.5
            "EGYPT" -> 19.5
            else -> 18.0 // Karachi (Islamic Sciences Karachi)
        }
        val hFajr = computeHourAngle(-fajrAngle, latitude, declination)
        val fajrHour = transit - hFajr / 15.0

        // Asr calculation
        val shadowFactor = if (asrMethod == "HANAFI") 2.0 else 1.0
        val asrAlt = radToDeg(atan(1.0 / (shadowFactor + tan(degToRad(abs(latitude - declination))))))
        val hAsr = computeHourAngle(asrAlt, latitude, declination)
        val asrHour = transit + hAsr / 15.0

        // Maghrib is sunset
        val maghribHour = sunsetHour

        // Isha calculation
        val ishaHour = if (calculationMethod == "MAKKAH") {
            maghribHour + 1.5 // 90 min after Maghrib
        } else {
            val ishaAngle = when (calculationMethod) {
                "MWL" -> 17.0
                "ISNA" -> 15.0
                "EGYPT" -> 17.5
                else -> 18.0 // Karachi
            }
            val hIsha = computeHourAngle(-ishaAngle, latitude, declination)
            transit + hIsha / 15.0
        }

        val fajrCal = setTimeToCalendar(calendar, fajrHour)
        val sunriseCal = setTimeToCalendar(calendar, sunriseHour)
        val dhuhrCal = setTimeToCalendar(calendar, transit)
        val asrCal = setTimeToCalendar(calendar, asrHour)
        val maghribCal = setTimeToCalendar(calendar, maghribHour)
        val ishaCal = setTimeToCalendar(calendar, ishaHour)

        val now = calendar.timeInMillis

        // Determine next prayer
        val nextPair: Pair<String, Calendar> = when {
            now < fajrCal.timeInMillis -> Pair("Fajr", fajrCal)
            now < dhuhrCal.timeInMillis -> Pair("Dhuhr", dhuhrCal)
            now < asrCal.timeInMillis -> Pair("Asr", asrCal)
            now < maghribCal.timeInMillis -> Pair("Maghrib", maghribCal)
            now < ishaCal.timeInMillis -> Pair("Isha", ishaCal)
            else -> {
                // Next is tomorrow's Fajr
                val tomorrowFajr = (fajrCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
                Pair("Fajr", tomorrowFajr)
            }
        }

        val countdown = maxOf(0L, nextPair.second.timeInMillis - now)

        val sdf = SimpleDateFormat(if (is24Hour) "HH:mm" else "hh:mm a", Locale.getDefault())
        val dateSdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())

        return PrayerTimesToday(
            fajr = sdf.format(fajrCal.time),
            sunrise = sdf.format(sunriseCal.time),
            dhuhr = sdf.format(dhuhrCal.time),
            asr = sdf.format(asrCal.time),
            maghrib = sdf.format(maghribCal.time),
            isha = sdf.format(ishaCal.time),
            fajrMillis = fajrCal.timeInMillis,
            sunriseMillis = sunriseCal.timeInMillis,
            dhuhrMillis = dhuhrCal.timeInMillis,
            asrMillis = asrCal.timeInMillis,
            maghribMillis = maghribCal.timeInMillis,
            ishaMillis = ishaCal.timeInMillis,
            nextPrayerName = nextPair.first,
            nextPrayerTime = sdf.format(nextPair.second.time),
            nextPrayerMillis = nextPair.second.timeInMillis,
            countdownMillis = countdown,
            dateGregorianFormatted = dateSdf.format(calendar.time),
            dateHijriFormatted = HijriCalendarHelper.getHijriDate(calendar)
        )
    }

    private fun computeJulianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun computeHourAngle(alpha: Double, lat: Double, delta: Double): Double {
        val cosH = (sin(degToRad(alpha)) - sin(degToRad(lat)) * sin(degToRad(delta))) /
                (cos(degToRad(lat)) * cos(degToRad(delta)))
        val clamped = cosH.coerceIn(-1.0, 1.0)
        return radToDeg(acos(clamped))
    }

    private fun setTimeToCalendar(baseCal: Calendar, hourFraction: Double): Calendar {
        val cal = baseCal.clone() as Calendar
        val h = floor(hourFraction).toInt()
        val m = floor((hourFraction - h) * 60.0).toInt()
        val s = floor(((hourFraction - h) * 60.0 - m) * 60.0).toInt()
        cal.set(Calendar.HOUR_OF_DAY, h.coerceIn(0, 23))
        cal.set(Calendar.MINUTE, m.coerceIn(0, 59))
        cal.set(Calendar.SECOND, s.coerceIn(0, 59))
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    private fun degToRad(deg: Double) = deg * Math.PI / 180.0
    private fun radToDeg(rad: Double) = rad * 180.0 / Math.PI
    private fun fixAngle(angle: Double): Double {
        var a = angle - 360.0 * floor(angle / 360.0)
        if (a < 0) a += 360.0
        return a
    }
    private fun fixHour(hour: Double): Double {
        var a = hour - 24.0 * floor(hour / 24.0)
        if (a < 0) a += 24.0
        return a
    }
}
