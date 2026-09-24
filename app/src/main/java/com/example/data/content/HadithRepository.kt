package com.example.data.content

data class HadithItem(
    val id: Int,
    val narratorBn: String,
    val narratorEn: String,
    val textArabic: String,
    val textBn: String,
    val textEn: String,
    val source: String,
    val topic: String
)

object HadithRepository {

    val HADITHS: List<HadithItem> = listOf(
        HadithItem(
            id = 1,
            narratorBn = "উমর ইবনুল খাত্তাব (রাঃ) থেকে বর্ণিত",
            narratorEn = "Narrated by Umar bin Al-Khattab (RA)",
            textArabic = "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            textBn = "নিশ্চয়ই সমস্ত কাজের ফলাফল নিয়তের ওপর নির্ভরশীল। আর প্রত্যেক ব্যক্তি তার নিয়ত অনুযায়ী প্রতিফল পাবে।",
            textEn = "Actions are according to intentions, and everyone will get what was intended.",
            source = "সহীহ বুখারী: ১, সহীহ মুসলিম: ১৯০৭",
            topic = "নিয়ত ও ইখলাস"
        ),
        HadithItem(
            id = 2,
            narratorBn = "আবু হুরায়রা (রাঃ) থেকে বর্ণিত",
            narratorEn = "Narrated by Abu Hurairah (RA)",
            textArabic = "الصَّلَوَاتُ الْخَمْسُ وَالْجُمُعَةُ إِلَى الْجُمُعَةِ كَفَّارَاتٌ لِمَا بَيْنَهُنَّ مَا لَمْ تُغْشَ الْكَبَائِرُ",
            textBn = "পাঁচ ওয়াক্ত সালাত এবং এক জুমুআ হতে অন্য জুমুআ—এর মধ্যবর্তী সমস্ত গুনাহের জন্য কাফফারা স্বরূপ, যতক্ষণ না সে কবিরা গুনাহে লিপ্ত হয়।",
            textEn = "The five daily prayers and Friday prayer to next Friday prayer are expiations for whatever sins happen between them, so long as major sins are avoided.",
            source = "সহীহ মুসলিম: ২৩৩",
            topic = "নামাজের মর্যাদা"
        ),
        HadithItem(
            id = 3,
            narratorBn = "উসমান ইবনু আফফান (রাঃ) থেকে বর্ণিত",
            narratorEn = "Narrated by Uthman bin Affan (RA)",
            textArabic = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            textBn = "তোমাদের মধ্যে সেই ব্যক্তি সর্বোত্তম, যে নিজে কুরআন শিখে এবং অপরকে তা শিক্ষা দেয়।",
            textEn = "The best among you are those who learn the Quran and teach it to others.",
            source = "সহীহ বুখারী: ৫০২৭",
            topic = "কুরআনের মর্যাদা"
        ),
        HadithItem(
            id = 4,
            narratorBn = "আবদুল্লাহ ইবনু মাসউদ (রাঃ) থেকে বর্ণিত",
            narratorEn = "Narrated by Abdullah bin Mas'ud (RA)",
            textArabic = "سَأَلْتُ النَّبِيَّ ﷺ: أَيُّ الْعَمَلِ أَحَبُّ إِلَى اللَّهِ؟ قَالَ: «الصَّلَاةُ عَلَى وَقْتِهَا»",
            textBn = "আমি রাসূলুল্লাহ (সাঃ)-কে জিজ্ঞেস করলাম, আল্লাহর নিকট সবচেয়ে প্রিয় আমল কোনটি? তিনি বললেন: 'সময়মত নামাজ আদায় করা'।",
            textEn = "I asked the Prophet (PBUH): Which deed is dearest to Allah? He replied: 'To perform prayers at their earliest fixed times.'",
            source = "সহীহ বুখারী: ৫২৭, সহীহ মুসলিম: ৮৫",
            topic = "সময়মত নামাজ"
        ),
        HadithItem(
            id = 5,
            narratorBn = "আনাস (রাঃ) থেকে বর্ণিত",
            narratorEn = "Narrated by Anas (RA)",
            textArabic = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            textBn = "তোমাদের কেউ ততক্ষণ পর্যন্ত প্রকৃত মুমিন হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করবে যা সে নিজের জন্য পছন্দ করে।",
            textEn = "None of you truly believes until he loves for his brother what he loves for himself.",
            source = "সহীহ বুখারী: ১৩, সহীহ মুসলিম: ৪৫",
            topic = "ভ্রাতৃত্ব ও মানবতা"
        )
    )

    fun getDailyHadith(): HadithItem = HADITHS[3] // "Prayer at its appointed time"
}
