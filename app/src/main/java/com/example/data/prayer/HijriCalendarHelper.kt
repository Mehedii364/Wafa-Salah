package com.example.data.prayer

import java.util.Calendar
import kotlin.math.floor

data class HijriDate(
    val day: Int,
    val monthIndex: Int, // 1 to 12
    val monthNameEn: String,
    val monthNameBn: String,
    val monthNameAr: String,
    val year: Int
)

object HijriCalendarHelper {

    private val MONTH_NAMES_EN = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    private val MONTH_NAMES_BN = listOf(
        "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    )

    private val MONTH_NAMES_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    fun getHijriDate(calendar: Calendar = Calendar.getInstance(), dayAdjustment: Int = 0): String {
        val hijri = convertGregorianToHijri(calendar, dayAdjustment)
        return "${hijri.day} ${hijri.monthNameBn} ${hijri.year} হিজরি"
    }

    fun getHijriDetails(calendar: Calendar = Calendar.getInstance(), dayAdjustment: Int = 0): HijriDate {
        return convertGregorianToHijri(calendar, dayAdjustment)
    }

    fun convertGregorianToHijri(calendar: Calendar, dayAdjustment: Int = 0): HijriDate {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)

        var year = y
        var month = m
        if (month < 3) {
            year -= 1
            month += 12
        }

        val a = floor(year / 100.0)
        val b = 2 - a + floor(a / 4.0)
        var jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b - 1524.5
        jd += dayAdjustment

        // Julian Day to Hijri
        val l = jd.toLong() - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val lPrime = l - 10631 * n + 354
        val j = (((10985 - lPrime) / 5316).toInt()) * ((50 * lPrime / 17719).toInt()) +
                ((lPrime / 5670).toInt()) * ((43 * lPrime / 15238).toInt())
        val lDoublePrime = lPrime - (((30 - j) / 15).toInt()) * ((17719 * j / 50).toInt()) -
                ((j / 16).toInt()) * ((15238 * j / 43).toInt()) + 29
        val hijriMonth = ((24 * lDoublePrime / 709).toInt()).coerceIn(1, 12)
        val hijriDay = (lDoublePrime - ((709 * hijriMonth / 24).toInt())).toInt().coerceIn(1, 30)
        val hijriYear = (30 * n + j - 30).toInt()

        val idx = (hijriMonth - 1).coerceIn(0, 11)
        return HijriDate(
            day = hijriDay,
            monthIndex = hijriMonth,
            monthNameEn = MONTH_NAMES_EN[idx],
            monthNameBn = MONTH_NAMES_BN[idx],
            monthNameAr = MONTH_NAMES_AR[idx],
            year = hijriYear
        )
    }

    fun getUpcomingEvents(year: Int): List<Triple<String, String, String>> {
        return listOf(
            Triple("Ashura (১০ মুহররম)", "10 Muharram", "রোজা রাখা মুস্তাহাব"),
            Triple("Shab-e-Barat (১৫ শাবান)", "15 Sha'ban", "ইবাদত ও মাগফিরাত"),
            Triple("পবিত্র রমজান শুরু", "1 Ramadan", "সিয়াম সাধনা"),
            Triple("Lailat al-Qadr (২৭ রমজান)", "27 Ramadan", "হাজার মাসের চেয়ে উত্তম রাত"),
            Triple("Eid-ul-Fitr (১ শাওয়াল)", "1 Shawwal", "পবিত্র ঈদুল ফিতর"),
            Triple("হজের দিন / আরাফাত", "9 Dhu al-Hijjah", "আরাফাতের দিন"),
            Triple("Eid-ul-Adha (১০ জিলহজ)", "10 Dhu al-Hijjah", "কুরবানির পবিত্র ঈদ")
        )
    }
}
