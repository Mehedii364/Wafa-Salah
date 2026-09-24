package com.example.data.content

data class NamazStep(
    val stepNumber: Int,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val arabic: String = "",
    val banglaPronunciation: String = "",
    val banglaMeaning: String = "",
    val englishMeaning: String = "",
    val menSpecificNote: String = "",
    val womenSpecificNote: String = ""
)

object NamazGuideRepository {

    val WUDU_STEPS: List<NamazStep> = listOf(
        NamazStep(
            stepNumber = 1,
            titleBn = "নিয়ত ও বিসমিল্লাহ",
            titleEn = "Intention & Bismillah",
            descriptionBn = "মনে মনে ওযুর নিয়ত করে 'বিসমিল্লাহ' বলে শুরু করা।",
            descriptionEn = "Make the intention in the heart for ablution and recite Bismillah.",
            arabic = "بِسْمِ اللَّهِ",
            banglaPronunciation = "বিসমিল্লাহ।",
            banglaMeaning = "আল্লাহর নামে শুরু করছি।",
            englishMeaning = "In the name of Allah."
        ),
        NamazStep(
            stepNumber = 2,
            titleBn = "দুই হাত কবজি পর্যন্ত ধোয়া",
            titleEn = "Washing Hands to Wrists",
            descriptionBn = "উভয় হাত কবজি পর্যন্ত ৩ বার উত্তমরূপে ধোয়া এবং আঙুলগুলো খিলাল করা।",
            descriptionEn = "Wash both hands thoroughly up to the wrists three times, interlacing fingers.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 3,
            titleBn = "কুলি করা ও মিসওয়াক",
            titleEn = "Rinsing the Mouth",
            descriptionBn = "ডান হাত দিয়ে মুখে পানি নিয়ে ৩ বার ভালোভাবে কুলি করা।",
            descriptionEn = "Rinse mouth thoroughly three times with the right hand.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 4,
            titleBn = "নাকে পানি দেওয়া",
            titleEn = "Sniffing Water into Nostrils",
            descriptionBn = "ডান হাতে নাকে পানি টেনে নিয়ে বাম হাত দিয়ে নাক ৩ বার পরিষ্কার করা।",
            descriptionEn = "Sniff water gently into nostrils using right hand and blow it out with left hand.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 5,
            titleBn = "সম্পূর্ণ মুখমণ্ডল ধোয়া",
            titleEn = "Washing the Face",
            descriptionBn = "কপালের চুলের গোড়া থেকে থুতনির নিচ এবং এক কানের লতি থেকে অপর কানের লতি পর্যন্ত সম্পূর্ণ মুখ ৩ বার ধোয়া।",
            descriptionEn = "Wash the entire face from hair line to chin and ear to ear three times.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 6,
            titleBn = "দুই হাত কনুইসহ ধোয়া",
            titleEn = "Washing Arms to Elbows",
            descriptionBn = "প্রথমে ডান হাত কনুইসহ ৩ বার এবং পরে বাম হাত কনুইসহ ৩ বার ধোয়া।",
            descriptionEn = "Wash right arm including elbow three times, then left arm three times.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 7,
            titleBn = "মাথা ও কান মাসেহ করা",
            titleEn = "Wiping Head & Ears",
            descriptionBn = "ভেজা হাত দিয়ে সম্পূর্ণ মাথা একবার সামনের দিক থেকে পেছনের দিকে এবং ভেতর থেকে কানের লতি মাসেহ করা।",
            descriptionEn = "Wipe wet hands over entire head from front to back, and wipe ears inside and out once.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        ),
        NamazStep(
            stepNumber = 8,
            titleBn = "দুই পা টাখনুসহ ধোয়া",
            titleEn = "Washing Feet to Ankles",
            descriptionBn = "প্রথমে ডান পা টাখনুসহ ৩ বার এবং পরে বাম পা টাখনুসহ ৩ বার আঙুল খিলাল করে ধোয়া।",
            descriptionEn = "Wash right foot up to ankle three times including between toes, then left foot.",
            arabic = "",
            banglaPronunciation = "",
            banglaMeaning = "",
            englishMeaning = ""
        )
    )

    val PRAYER_STEPS: List<NamazStep> = listOf(
        NamazStep(
            stepNumber = 1,
            titleBn = "তাকবীরে তাহরীমা",
            titleEn = "Takbirat al-Ihram",
            descriptionBn = "ক্বিবলামুখী হয়ে দাঁড়ানো এবং 'আল্লাহু আকবার' বলে নামাজ শুরু করা।",
            descriptionEn = "Face the Qibla and begin prayer by proclaiming Allahu Akbar.",
            arabic = "اللَّهُ أَكْبَرُ",
            banglaPronunciation = "আল্লাহু আকবার।",
            banglaMeaning = "আল্লাহ মহান।",
            englishMeaning = "Allah is the Greatest.",
            menSpecificNote = "পুরুষরা দুই হাত কানের লতি পর্যন্ত তুলবেন এবং নাভির নিচে হাত বাঁধিবেন।",
            womenSpecificNote = "মহিলারা দুই হাত কাঁধ বরাবর তুলবেন এবং বুকের ওপর হাত রাখবেন।"
        ),
        NamazStep(
            stepNumber = 2,
            titleBn = "সানা পাঠ",
            titleEn = "Thana (Opening Supplication)",
            descriptionBn = "হাত বাঁধার পর সানাহ পাঠ করা।",
            descriptionEn = "Recite the opening glorification of Allah.",
            arabic = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلَٰهَ غَيْرُكَ",
            banglaPronunciation = "সুবহানাকাল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা ইলাহা গাইরুকা।",
            banglaMeaning = "হে আল্লাহ! আপনার পবিত্রতা ঘোষণা করছি এবং আপনার প্রশংসা করছি। আপনার নাম পরম বরকতময়, আপনার মর্যাদা সুউচ্চ এবং আপনি ছাড়া কোনো ইলাহ নেই।",
            englishMeaning = "Glory be to You, O Allah, and all praises are for You, and blessed is Your name, and high is Your majesty and there is no god besides You."
        ),
        NamazStep(
            stepNumber = 3,
            titleBn = "ক্বিয়াম ও কিরাত (সূরা ফাতিহা ও সূরা পাঠ)",
            titleEn = "Qiyam & Recitation",
            descriptionBn = "আ'ঊযু বিল্লাহ ও বিসমিল্লাহ পড়ে সূরা আল-ফাতিহা এবং অন্য একটি সূরা বা কয়েকটি আয়াত মিলিয়ে পাঠ করা।",
            descriptionEn = "Recite Surah Al-Fatihah followed by another Surah or portion from the Holy Quran.",
            arabic = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ • بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            banglaPronunciation = "আঊযু বিল্লাহি মিনাশ শাইতানির রাজীম। বিসমিল্লাহির রাহমানির রাহিম।",
            banglaMeaning = "বিতাড়িত শয়তান থেকে আল্লাহর আশ্রয় চাই। পরম করুণাময় আল্লাহর নামে শুরু করছি।",
            englishMeaning = "I seek refuge in Allah from Satan the outcast. In the name of Allah, the Most Merciful."
        ),
        NamazStep(
            stepNumber = 4,
            titleBn = "রুকু (অবনত হওয়া)",
            titleEn = "Ruku (Bowing)",
            descriptionBn = "'আল্লাহু আকবার' বলে রুকুতে যাওয়া এবং হাঁটু শক্ত করে আঁকড়ে ধরে পিঠ সোজা রাখা।",
            descriptionEn = "Say Allahu Akbar and bow with back level, holding the knees.",
            arabic = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
            banglaPronunciation = "সুবহানা রব্বিয়াল 'আযীম (কমপক্ষে ৩ বার)।",
            banglaMeaning = "আমার সুমহান প্রতিপালকের পবিত্রতা ঘোষণা করছি।",
            englishMeaning = "Glory be to my Lord, the Magnificent (at least 3 times).",
            menSpecificNote = "পুরুষরা পিঠ একেবারে সোজা রাখবেন এবং কনুই ফাঁক রাখবেন।",
            womenSpecificNote = "মহিলারা সামান্য ঝুঁকবেন যাতে হাত হাঁটুর ওপর পৌঁছে এবং শরীর সংকুচিত রাখবেন।"
        ),
        NamazStep(
            stepNumber = 5,
            titleBn = "ক্বওমা (রুকু থেকে সোজা হয়ে দাঁড়ানো)",
            titleEn = "Qawmah (Standing after Bowing)",
            descriptionBn = "সোজা হয়ে দাঁড়িয়ে তাসমী' ও তাহমীদ পাঠ করা।",
            descriptionEn = "Rise upright from bowing praising Allah.",
            arabic = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ • رَبَّنَا لَكَ الْحَمْدُ",
            banglaPronunciation = "সামি'আল্লাহু লিমান হামিদাহ। রব্বানা লাকাল হামদ।",
            banglaMeaning = "আল্লাহ তাঁর কথা শুনেন যে তাঁর প্রশংসা করে। হে আমাদের প্রতিপালক! আপনার জন্যই সমস্ত প্রশংসা।",
            englishMeaning = "Allah hears whoever praises Him. Our Lord, to You is all praise."
        ),
        NamazStep(
            stepNumber = 6,
            titleBn = "সিজদা",
            titleEn = "Sujood (Prostration)",
            descriptionBn = "'আল্লাহু আকবার' বলে হাঁটু, হাত, নাক ও কপাল মাটিতে রেখে সিজদায় অবনত হওয়া।",
            descriptionEn = "Prostrate on seven bones: forehead & nose, both hands, knees, and toes.",
            arabic = "سُبْحَانَ رَبِّيَ الْأَعْلَىٰ",
            banglaPronunciation = "সুবহানা রব্বিয়াল আ'লা (কমপক্ষে ৩ বার)।",
            banglaMeaning = "আমার সর্বশ্রেষ্ঠ মহান প্রতিপালকের পবিত্রতা বর্ণনা করছি।",
            englishMeaning = "Glory be to my Lord, the Most High (at least 3 times).",
            menSpecificNote = "পুরুষরা পেটকে উরু থেকে এবং কনুইকে জমিন ও বগল থেকে আলাদা রাখবেন।",
            womenSpecificNote = "মহিলারা সম্পূর্ণ জড়সড় হয়ে সিজদা করবেন, বাহু জমিনের সাথে ও পেট উরুর সাথে লাগিয়ে রাখবেন।"
        ),
        NamazStep(
            stepNumber = 7,
            titleBn = "জলসা (দুই সিজদার মধ্যবর্তী বৈঠক)",
            titleEn = "Jalsah (Sitting between Sujood)",
            descriptionBn = "সোজা হয়ে বসে শান্তভাবে ক্ষমা প্রার্থনা করা এবং অতঃপর দ্বিতীয় সিজদা আদায় করা।",
            descriptionEn = "Sit calmly between the two prostrations and supplicate for forgiveness.",
            arabic = "رَبِّ اغْفِرْ لِي وَارْحَمْنِي",
            banglaPronunciation = "রব্বিগফির লী ওয়ারহামনী।",
            banglaMeaning = "হে আমার রব! আমাকে ক্ষমা করুন এবং আমার ওপর রহমত বর্ষণ করুন।",
            englishMeaning = "My Lord, forgive me and have mercy on me."
        ),
        NamazStep(
            stepNumber = 8,
            titleBn = "তাশাহহুদ (ক্বা'দাহ বা শেষ বৈঠক)",
            titleEn = "Tashahhud (Final Sitting)",
            descriptionBn = "শেষ বৈঠকে তাশাহহুদ, দরূদ শরীফ ও দোয়া মাসূরা পাঠ করা।",
            descriptionEn = "In the sitting posture, recite Tashahhud, Salawat on Prophet (Durood), and Dua Masura.",
            arabic = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            banglaPronunciation = "আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত-ত্বাইয়্যিবাতু, আসসালামু আলাইকা আইয়্যুহান-নাবিয়্যু ওয়া রাহমাতুল্লাহি ওয়া বারাকাতুহু, আসসালামু আলাইনা ওয়া আলা ইবাদিল্লাহিস সালিহীন, আশহাদু আল-লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসূলুহু।",
            banglaMeaning = "যাবতীয় সম্মান, নামাজ ও পবিত্র বিষয় আল্লাহর জন্য। হে নবী! আপনার প্রতি শান্তি, আল্লাহর রহমত ও বরকত বর্ষিত হোক। আমাদের প্রতি এবং আল্লাহর নেক বান্দাদের প্রতিও শান্তি বর্ষিত হোক। আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া কোনো উপাস্য নেই এবং মুহাম্মদ (সাঃ) তাঁর বান্দা ও রাসূল।",
            englishMeaning = "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and Allah's mercy and blessings. Peace be upon us and upon righteous servants of Allah. I bear witness that none has right to be worshipped except Allah, and Muhammad is His slave and Messenger.",
            menSpecificNote = "পুরুষরা বাম পায়ের ওপর বসবেন এবং ডান পা খাড়া রাখবেন।",
            womenSpecificNote = "মহিলারা উভয় পা ডান দিকে বের করে নিতম্বের ওপর বসবেন।"
        ),
        NamazStep(
            stepNumber = 9,
            titleBn = "সালাম ফিরিয়ে নামাজ সমাপ্তি",
            titleEn = "Tasleem (Concluding the Prayer)",
            descriptionBn = "প্রথমে ডানে মুখ ফিরিয়ে এবং পরে বামে মুখ ফিরিয়ে সালাম দেওয়া।",
            descriptionEn = "Turn face to the right saying Salam, then to the left concluding the prayer.",
            arabic = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
            banglaPronunciation = "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ।",
            banglaMeaning = "আপনাদের ওপর শান্তি ও আল্লাহর রহমত বর্ষিত হোক।",
            englishMeaning = "May peace and mercy of Allah be upon you."
        )
    )
}
