package com.example.data.dua

object DuaRepository {

    val CATEGORIES = listOf(
        "দৈনন্দিন" to "Daily",
        "সকাল-সন্ধ্যা" to "Morning & Evening",
        "নামাজ" to "Prayer",
        "সফর" to "Travel",
        "খাবার" to "Food",
        "ঘুম" to "Sleep",
        "পিতা-মাতা" to "Parents",
        "জ্ঞান ও পড়াশোনা" to "Study",
        "বিপদ ও সুরক্ষা" to "Protection",
        "রমজান" to "Ramadan",
        "ক্ষমা প্রার্থনা" to "Forgiveness",
        "সাধারণ" to "General"
    )

    val ALL_DUAS: List<DuaItem> = listOf(
        // Daily
        DuaItem(
            id = 1,
            category = "Daily",
            titleBn = "ঘর থেকে বের হওয়ার দোয়া",
            titleEn = "Leaving the House",
            arabic = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            banglaPronunciation = "বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহ, ওয়ালা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ।",
            englishMeaning = "In the name of Allah, I place my trust in Allah; there is no might and no power except by Allah.",
            banglaMeaning = "আল্লাহর নামে, আল্লাহর ওপরই আমি ভরসা করলাম। আল্লাহর সাহায্য ছাড়া পাপ থেকে বাঁচার এবং সৎকাজ করার কোনো শক্তি নেই।",
            reference = "আবু দাউদ: ৫০৯৫, তিরমিজি: ৩৪২৬",
            audioUrl = ""
        ),
        DuaItem(
            id = 2,
            category = "Daily",
            titleBn = "ঘরে প্রবেশের দোয়া",
            titleEn = "Entering the House",
            arabic = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى اللَّهِ رَبِّنَا تَوَكَّلْنَا",
            banglaPronunciation = "বিসমিল্লাহি ওয়ালাজনা, ওয়া বিসমিল্লাহি খারাজনা, ওয়া আলাল্লাহি রাব্বিনা তাওয়াক্কালনা।",
            englishMeaning = "In the name of Allah we enter, and in the name of Allah we leave, and upon Allah our Lord we rely.",
            banglaMeaning = "আল্লাহর নামে আমরা প্রবেশ করলাম, আল্লাহর নামেই বের হলাম এবং আমাদের প্রতিপালক আল্লাহর ওপরই ভরসা করলাম।",
            reference = "আবু দাউদ: ৫০৯৬",
            audioUrl = ""
        ),
        DuaItem(
            id = 3,
            category = "Daily",
            titleBn = "মসজিদে প্রবেশের দোয়া",
            titleEn = "Entering the Mosque",
            arabic = "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            banglaPronunciation = "আল্লাহুম্মাফ তাহলী আবওয়াবা রাহমাতিকা।",
            englishMeaning = "O Allah, open for me the doors of Your mercy.",
            banglaMeaning = "হে আল্লাহ! আমার জন্য আপনার রহমতের দরজাসমূহ উন্মুক্ত করে দিন।",
            reference = "সহীহ মুসলিম: ৭১৩",
            audioUrl = ""
        ),
        DuaItem(
            id = 4,
            category = "Daily",
            titleBn = "মসজিদ থেকে বের হওয়ার দোয়া",
            titleEn = "Leaving the Mosque",
            arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ",
            banglaPronunciation = "আল্লাহুম্মা ইন্নি আসআলুকা মিন ফাদলিকা।",
            englishMeaning = "O Allah, I ask You from Your bounty.",
            banglaMeaning = "হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে আপনার অনুগ্রহ প্রার্থনা করছি।",
            reference = "সহীহ মুসলিম: ৭১৩",
            audioUrl = ""
        ),
        DuaItem(
            id = 5,
            category = "Daily",
            titleBn = "টয়লেটে প্রবেশের দোয়া",
            titleEn = "Entering Toilet",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ",
            banglaPronunciation = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল খুবুসি ওয়াল খাবা-ইছ।",
            englishMeaning = "O Allah, I seek refuge in You from bad and harmful spirits.",
            banglaMeaning = "হে আল্লাহ! আমি আপনার নিকট অপবিত্র পুরুষ ও নারী জিনদের অনিষ্ট থেকে আশ্রয় চাই।",
            reference = "সহীহ বুখারী: ১৪২, মুসলিম: ৩৭৫",
            audioUrl = ""
        ),
        DuaItem(
            id = 6,
            category = "Daily",
            titleBn = "টয়লেট থেকে বের হওয়ার দোয়া",
            titleEn = "Leaving Toilet",
            arabic = "غُفْرَانَكَ",
            banglaPronunciation = "গুফরা-নাক।",
            englishMeaning = "I ask You (Allah) for forgiveness.",
            banglaMeaning = "হে আল্লাহ! আমি আপনার নিকট ক্ষমা প্রার্থনা করছি।",
            reference = "আবু দাউদ: ১৭, তিরমিজি: ৭",
            audioUrl = ""
        ),

        // Morning & Evening
        DuaItem(
            id = 7,
            category = "Morning",
            titleBn = "সকালের প্রধান দোয়া",
            titleEn = "Morning Supplication",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
            banglaPronunciation = "আসবাহনা ওয়া আসবাহাল মুলকু লিল্লাহ, ওয়াল হামদু লিল্লাহ, লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু।",
            englishMeaning = "We have entered the morning and the kingdom belongs to Allah; all praise is for Allah. There is no god but Allah alone, without any partner.",
            banglaMeaning = "আমরা সকালে উপনীত হয়েছি এবং সমগ্র রাজত্বও আল্লাহর জন্য সকালে উপনীত হয়েছে। সমস্ত প্রশংসা আল্লাহর, আল্লাহ ব্যতীত কোনো উপাস্য নেই, তিনি এক ও তাঁর কোনো শরিক নেই।",
            reference = "সহীহ মুসলিম: ২৭২৩",
            audioUrl = ""
        ),
        DuaItem(
            id = 8,
            category = "Evening",
            titleBn = "সন্ধ্যার প্রধান দোয়া",
            titleEn = "Evening Supplication",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
            banglaPronunciation = "আমসাইনা ওয়া আমসাল মুলকু লিল্লাহ, ওয়াল হামদু লিল্লাহ, লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু।",
            englishMeaning = "We have reached the evening and the dominion belongs to Allah; praise be to Allah. There is no deity except Allah alone.",
            banglaMeaning = "আমরা সন্ধ্যায় উপনীত হয়েছি এবং আল্লাহর রাজ্যও সন্ধ্যায় উপনীত হয়েছে। সমস্ত প্রশংসা আল্লাহর, আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি একক।",
            reference = "সহীহ মুসলিম: ২৭২৩",
            audioUrl = ""
        ),
        DuaItem(
            id = 9,
            category = "Morning",
            titleBn = "সাইয়্যিদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
            titleEn = "Sayyidul Istighfar",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            banglaPronunciation = "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা খালাকতানী ওয়া আনা আবদুকা, ওয়া আনা আলা আহদিকা ওয়া ওয়া'দিকা মাসতাতাতু, আঊযু বিকা মিন শাররি মা সানাতু, আবূউ লাকা বিনি'মাতিকা আলাইয়্যা, ওয়া আবূউ বিযানবী ফাগফিরলী ফাইন্নাহু লা ইয়াগফিরুয যুনূবা ইল্লা আনতা।",
            englishMeaning = "O Allah, You are my Lord; none has the right to be worshipped but You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can...",
            banglaMeaning = "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো ইলাহ নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি আমার সাধ্যানুযায়ী আপনার প্রতিশ্রুতিতে অঙ্গীকারাবদ্ধ আছি। আমি আমার কৃতকর্মের অনিষ্ট থেকে আপনার কাছে আশ্রয় চাই। আমার ওপর আপনার যে নিয়ামত রয়েছে তা আমি স্বীকার করছি এবং আমার অপরাধও স্বীকার করছি। অতএব আমাকে ক্ষমা করুন, কারণ আপনি ছাড়া পাপ ক্ষমা করার কেউ নেই।",
            reference = "সহীহ বুখারী: ৬৩০৬",
            audioUrl = ""
        ),

        // Food
        DuaItem(
            id = 10,
            category = "Food",
            titleBn = "খাবার শুরুর দোয়া",
            titleEn = "Before Eating",
            arabic = "بِسْمِ اللَّهِ",
            banglaPronunciation = "বিসমিল্লাহ।",
            englishMeaning = "In the name of Allah.",
            banglaMeaning = "আল্লাহর নামে শুরু করছি।",
            reference = "সহীহ বুখারী: ৫৩৭৬",
            audioUrl = ""
        ),
        DuaItem(
            id = 11,
            category = "Food",
            titleBn = "শুরুতে বিসমিল্লাহ ভুলে গেলে",
            titleEn = "If Forgotten at the Beginning",
            arabic = "بِسْمِ اللَّهِ أَوَّلَهُ وَآخِرَهُ",
            banglaPronunciation = "বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু।",
            englishMeaning = "In the name of Allah at the beginning and the end.",
            banglaMeaning = "আল্লাহর নামে এর শুরু ও শেষে।",
            reference = "আবু দাউদ: ৩৭৬৭, তিরমিজি: ১৮৫৮",
            audioUrl = ""
        ),
        DuaItem(
            id = 12,
            category = "Food",
            titleBn = "খাবার শেষের দোয়া",
            titleEn = "After Eating",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ",
            banglaPronunciation = "আলহামদু লিল্লাহিল্লাযী আত'আমানা ওয়া সাক্বানা ওয়া জা'আলানা মুসলিমীন।",
            englishMeaning = "Praise be to Allah Who has fed us, given us drink, and made us Muslims.",
            banglaMeaning = "সকল প্রশংসা আল্লাহর জন্য যিনি আমাদেরকে আহার করালেন, পান করালেন এবং মুসলিম বানালেন।",
            reference = "আবু দাউদ: ৩৮৫০, তিরমিজি: ৩৪৫৭",
            audioUrl = ""
        ),

        // Sleep
        DuaItem(
            id = 13,
            category = "Sleep",
            titleBn = "ঘুমাতে যাওয়ার দোয়া",
            titleEn = "Before Sleeping",
            arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            banglaPronunciation = "বিসমিকাল্লাহুম্মা আমূতু ওয়া আহ্ইয়া।",
            englishMeaning = "In Your Name, O Allah, I die and I live.",
            banglaMeaning = "হে আল্লাহ! আপনারই নামে আমি মৃত্যুবরণ (ঘুমাই) করি এবং জীবিত (জাগ্রত) হই।",
            reference = "সহীহ বুখারী: ৬৩১২, মুসলিম: ২৭১১",
            audioUrl = ""
        ),
        DuaItem(
            id = 14,
            category = "Sleep",
            titleBn = "ঘুম থেকে ওঠার দোয়া",
            titleEn = "Waking Up",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            banglaPronunciation = "আলহামদু লিল্লাহিল্লাযী আহইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশুর।",
            englishMeaning = "All praise is for Allah who gave us life after causing us to die, and unto Him is the resurrection.",
            banglaMeaning = "সমস্ত প্রশংসা আল্লাহর জন্য, যিনি আমাদেরকে মৃত্যু (নিদ্রা) দেওয়ার পর পুনর্জীবিত করলেন এবং তাঁরই কাছে সবার প্রত্যাবর্তন।",
            reference = "সহীহ বুখারী: ৬৩১২",
            audioUrl = ""
        ),

        // Parents
        DuaItem(
            id = 15,
            category = "Parents",
            titleBn = "পিতা-মাতার জন্য কুরআনী দোয়া",
            titleEn = "Supplication for Parents",
            arabic = "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            banglaPronunciation = "রব্বির হামহুমা কামা রব্বায়ানী সাগীরা।",
            englishMeaning = "My Lord, have mercy upon them as they brought me up [when I was] small.",
            banglaMeaning = "হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি রহম করুন, যেমন তাঁরা শৈশবে আমাকে স্নেহ-মমতায় লালন-পালন করেছেন।",
            reference = "সূরা বনি ইসরাঈল: ২৪",
            audioUrl = ""
        ),
        DuaItem(
            id = 16,
            category = "Parents",
            titleBn = "পিতা-মাতা ও মুমিনদের ক্ষমার দোয়া",
            titleEn = "Forgiveness for Parents & Believers",
            arabic = "رَبَّنَا اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ يَوْمَ يَقُومُ الْحِسَابُ",
            banglaPronunciation = "রব্বানাগফিরলী ওয়ালি ওয়ালিদাইয়্যা ওয়া লিলমু'মিনীনা ইয়াওমা ইয়াকূমুল হিসার।",
            englishMeaning = "Our Lord, forgive me and my parents and the believers the Day the account is established.",
            banglaMeaning = "হে আমাদের পালনকর্তা! যেদিন হিসাব কায়েম হবে, সেদিন আমাকে, আমার পিতা-মাতাকে এবং সব মুমিনকে ক্ষমা করে দেবেন।",
            reference = "সূরা ইব্রাহিম: ৪১",
            audioUrl = ""
        ),

        // Study
        DuaItem(
            id = 17,
            category = "Study",
            titleBn = "জ্ঞান বৃদ্ধির দোয়া",
            titleEn = "Increase in Knowledge",
            arabic = "رَّبِّ زِدْنِي عِلْمًا",
            banglaPronunciation = "রব্বি যিদনী ইলমা।",
            englishMeaning = "My Lord, increase me in knowledge.",
            banglaMeaning = "হে আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করে দিন।",
            reference = "সূরা ত্বোয়া-হা: ১১৪",
            audioUrl = ""
        ),
        DuaItem(
            id = 18,
            category = "Study",
            titleBn = "বক্ষ প্রশস্ত ও কথা স্পষ্ট হওয়ার দোয়া",
            titleEn = "Clarity and Ease in Speech",
            arabic = "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي يَفْقَهُوا قَوْلِي",
            banglaPronunciation = "রব্বিশ রাহলী সাদরী, ওয়া ইয়াসসিরলী আমরী, ওয়াহলুল উকদাতাম মিল লিসানী, ইয়াফকাহু ক্বওলী।",
            englishMeaning = "My Lord, expand for me my chest, and ease for me my task, and untie the knot from my tongue that they may understand my speech.",
            banglaMeaning = "হে আমার পালনকর্তা! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করে দিন এবং আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে।",
            reference = "সূরা ত্বোয়া-হা: ২৫-২৮",
            audioUrl = ""
        ),

        // Travel
        DuaItem(
            id = 19,
            category = "Travel",
            titleBn = "বাহনে আরোহণের দোয়া",
            titleEn = "Mounting a Vehicle / Travel",
            arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ",
            banglaPronunciation = "সুবহানাল্লাযী সাখখারা লানা হাযা ওয়ামা কুন্না লাহু মুকরিনীনা, ওয়া ইন্না ইলা রব্বিনা লামুনকালিবুন।",
            englishMeaning = "Glory to Him who has brought this into subjection for us though we were unable to subdue it, and indeed to our Lord we shall return.",
            banglaMeaning = "পবিত্র ও মহান তিনি, যিনি এটাকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা এটিকে নিয়ন্ত্রণে সমর্থ ছিলাম না। আর আমরা নিশ্চয়ই আমাদের প্রতিপালকের নিকট প্রত্যাবর্তন করব।",
            reference = "সূরা জুখরুফ: ১৩-১৪, সহীহ মুসলিম: ১৩৪২",
            audioUrl = ""
        ),

        // Protection
        DuaItem(
            id = 20,
            category = "Protection",
            titleBn = "বিপদ ও ক্ষতি থেকে রক্ষার দোয়া",
            titleEn = "Protection Against Harm",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            banglaPronunciation = "বিসমিল্লাহিল্লাযী লা ইয়াদুররু মা'আসমিহী শাইউন ফিল আরদি ওয়ালা ফিস সামা-ই ওয়াহুওয়াস সামী'উল আলীম।",
            englishMeaning = "In the Name of Allah, with whose name nothing on earth or in heaven can cause harm, and He is the All-Hearing, All-Knowing.",
            banglaMeaning = "আল্লাহর নামে, যাঁর নামের বরকতে আসমান ও জমিনের কোনো কিছুই কোনো ক্ষতি করতে পারে না। আর তিনি সর্বশ্রোতা, মহাজ্ঞানী। (সকাল-সন্ধ্যায় ৩ বার পাঠ্য)",
            reference = "আবু দাউদ: ৫০৮৮, তিরমিজি: ৩৩৮৮",
            audioUrl = ""
        ),
        DuaItem(
            id = 21,
            category = "Protection",
            titleBn = "চিন্তা ও ঋণমুক্তির দোয়া",
            titleEn = "Relief from Anxiety and Debt",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
            banglaPronunciation = "আল্লাহুম্মা ইন্নী আঊযু বিকা মিনাল হামমি ওয়াল হাযান, ওয়াল আজযি ওয়াল কাসাল, ওয়াল বুখলি ওয়াল জুবন, ওয়া দ্বালা'ইদ দাইনি ওয়া গালাবাতির রিজাল।",
            englishMeaning = "O Allah, I seek refuge in You from grief and sadness, weakness and laziness, miserliness and cowardice, the burden of debt, and being overpowered by men.",
            banglaMeaning = "হে আল্লাহ! আমি আপনার আশ্রয় নিচ্ছি দুশ্চিন্তা ও পেরেশানি থেকে, অক্ষমতা ও অলসতা থেকে, কৃপণতা ও কাপুরুষতা থেকে, ঋণের বোঝা ও মানুষের প্রাধান্য/অত্যাচার থেকে।",
            reference = "সহীহ বুখারী: ২৮৯৩",
            audioUrl = ""
        ),

        // Ramadan
        DuaItem(
            id = 22,
            category = "Ramadan",
            titleBn = "ইফতারের দোয়া",
            titleEn = "Dua for Breaking Fast (Iftar)",
            arabic = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
            banglaPronunciation = "যাহাবায জামা'উ ওয়াবতাল্লাতিল উরূকু ওয়া ছাবাতাল আজরু ইনশাআল্লাহ।",
            englishMeaning = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            banglaMeaning = "পিপাসা দূর হলো, শিরা-উপশিরা সিক্ত হলো এবং আল্লাহর ইচ্ছায় সওয়াব নির্ধারিত হলো।",
            reference = "আবু দাউদ: ২৩৫৭",
            audioUrl = ""
        ),
        DuaItem(
            id = 23,
            category = "Ramadan",
            titleBn = "রোজার নিয়তের দোয়া",
            titleEn = "Intention for Fasting",
            arabic = "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ",
            banglaPronunciation = "নাওয়াইতু আন আসূমা গাদান মিন শাহরি রামাদানাল মুবারাকি ফারদান লাকা ইয়া আল্লাহ।",
            englishMeaning = "I intend to fast tomorrow in the blessed month of Ramadan for Your sake, O Allah.",
            banglaMeaning = "হে আল্লাহ! আমি আপনার সন্তুষ্টির উদ্দেশ্যে আগামীকালের পবিত্র রমজানের ফরজ রোজা রাখার নিয়ত করলাম।",
            reference = "নিয়তের সাধারণ বাক্য (অন্তরের সংকল্পই মূল)",
            audioUrl = ""
        ),

        // Forgiveness
        DuaItem(
            id = 24,
            category = "Forgiveness",
            titleBn = "ইউনুস (আঃ)-এর দোয়া (দোয়ায়ে ইউনুস)",
            titleEn = "Dua of Prophet Yunus",
            arabic = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            banglaPronunciation = "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নী কুন্তু মিনায যলিমীন।",
            englishMeaning = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            banglaMeaning = "আপনি ব্যতীত কোনো সত্য উপাস্য নেই, আপনি পবিত্র মহান! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গেছি।",
            reference = "সূরা আম্বিয়া: ৮৭, তিরমিজি: ৩৫০৫",
            audioUrl = ""
        ),
        DuaItem(
            id = 25,
            category = "General",
            titleBn = "দুনিয়া ও আখিরাতের কল্যাণের দোয়া",
            titleEn = "Goodness in this Life and Hereafter",
            arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            banglaPronunciation = "রব্বানা আতিনা ফিদ-দুনিয়া হাসানাতাঁও ওয়াফিল আখিরাতি হাসানাতাঁও ওয়াক্বিনা আযাবান নার।",
            englishMeaning = "Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the punishment of the Fire.",
            banglaMeaning = "হে আমাদের প্রভু! আমাদের ইহকালে কল্যাণ দিন এবং পরকালেও কল্যাণ দান করুন আর আমাদেরকে জাহান্নামের ভয়াবহ আযাব থেকে রক্ষা করুন।",
            reference = "সূরা বাকারা: ২০১, সহীহ বুখারী: ৪৫২২",
            audioUrl = ""
        )
    )

    fun getDailyDua(): DuaItem = ALL_DUAS.first { it.id == 25 }
}
