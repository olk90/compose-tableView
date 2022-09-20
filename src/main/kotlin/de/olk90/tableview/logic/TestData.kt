package de.olk90.tableview.logic

import kotlin.random.Random


val addresses = listOf(
    Address("Germany", "Hessen", "Wiesestadt", "49206", "Leinestr.", 64),
    Address("Latvia", "Valkas raj.", "Priekule", "LV7953", "Atgriežu bulvāris", 69),
    Address("South Africa", null, "Port Shannonland", "53886", "Grant Union", 38166),
    Address("Germany", "Brandenburg", "Neuohrland", "05216", "Am Telegraf", 99),
    Address("Belgium", "West-Vlaanderen", "Limburg", "3310", "Noahdreef", 160),
    Address("Norway", null, "Fagerborg", "6480", "Smedsrudgata", 6),
    Address("UK", null, "Marquardtbury", "GV56 9BH", "Hyatt Extension", 8523),
    Address("UK", null, "Jeffryhaven", "YD22 4BW", "Ike Spurs", 5841),
    Address("Germany", "North Rhine-Westphalia", "Julieburg", "24552", "Im Steinfeld", 311),
    Address("Belgium", "Oost-Vlaanderen", "Antwerpen", "8114", "Wannesplein", 951),
    Address("UK", null, "Bergemouth", "ED08 1RV", "Bettie Divide", 896),
    Address("Norway", null, "Gjesodden", "1522", "Jørgensvei", 12),
    Address("Germany", "Rhineland-Palatinate", "Neuohrland", "83121", "Hahnenblecher", 34),
    Address("UK", null, "North Stevie", "EA6 5KR", "Abbott Mountain", 3975),
    Address("Norway", null, "Storbø", "9405", "Bekkestykket", 52)
)

val persons = listOf(
    Person("Dong", "Cannon", Random.nextInt(20, 70), addresses[0]),
    Person("Roderick", "Huang", Random.nextInt(20, 70), addresses[1]),
    Person("Aurelia", "Beard", Random.nextInt(20, 70), addresses[2]),
    Person("Alvin", "Mayer", Random.nextInt(20, 70), addresses[3]),
    Person("Faustino", "Arellano", Random.nextInt(20, 70), addresses[4]),
    Person("Mauricio", "Pena", Random.nextInt(20, 70), addresses[5]),
    Person("Jami", "Walsh", Random.nextInt(20, 70), addresses[6]),
    Person("Julianne", "Owen", Random.nextInt(20, 70), addresses[7]),
    Person("Orville", "Burns", Random.nextInt(20, 70), addresses[8]),
    Person("Greta", "Marquez", Random.nextInt(20, 70), addresses[9]),
    Person("Marguerite", "Rush", Random.nextInt(20, 70), addresses[10]),
    Person("Kerri", "Holder", Random.nextInt(20, 70), addresses[11]),
    Person("Daniel", "Macias", Random.nextInt(20, 70), addresses[12]),
    Person("Paula", "Washington", Random.nextInt(20, 70), addresses[13]),
    Person("Aida", "Lane", Random.nextInt(20, 70), addresses[14])
)