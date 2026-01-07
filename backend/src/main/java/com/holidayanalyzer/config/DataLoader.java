package com.holidayanalyzer.config;

import com.holidayanalyzer.model.Country;
import com.holidayanalyzer.model.Region;
import com.holidayanalyzer.model.SchoolHoliday;
import com.holidayanalyzer.repository.CountryRepository;
import com.holidayanalyzer.repository.RegionRepository;
import com.holidayanalyzer.repository.SchoolHolidayRepository;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final SchoolHolidayRepository schoolHolidayRepository;

    public DataLoader(CountryRepository countryRepository, RegionRepository regionRepository, SchoolHolidayRepository schoolHolidayRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.schoolHolidayRepository = schoolHolidayRepository;
    }

    @Override
    public void run(String... args) {
        if (countryRepository.count() > 0) {
            log.info("Data already loaded, skipping initialization");
            return;
        }

        log.info("Loading initial data...");
        
        // Load countries
        Country de = createCountry("DE", "Germany", 83240000L);
        Country at = createCountry("AT", "Austria", 9006000L);
        Country ch = createCountry("CH", "Switzerland", 8740000L);
        Country fr = createCountry("FR", "France", 67410000L);
        Country es = createCountry("ES", "Spain", 47350000L);
        Country nl = createCountry("NL", "Netherlands", 17530000L);
        Country it = createCountry("IT", "Italy", 59070000L);
        Country pl = createCountry("PL", "Poland", 36753736L);
        Country no = createCountry("NO", "Norway", 5576660L);
        Country ru = createCountry("RU", "Russia", 144820423L);
        Country tr = createCountry("TR", "Turkey", 87473805L);
        Country gb = createCountry("GB", "United Kingdom", 69138192L);
        Country ua = createCountry("UA", "Ukraine", 37860221L);
        Country ro = createCountry("RO", "Romania", 19015088L);
        Country bel = createCountry("BE", "Belgium", 11738763L);
        Country cz = createCountry("CZ", "Czech Republic", 10735859L);
        Country se = createCountry("SE", "Sweden", 10590927L);
        Country pt = createCountry("PT", "Portugal", 10425292L);
        Country gr = createCountry("GR", "Greece", 10047817L);
        Country hu = createCountry("HU", "Hungary", 9676135L);
        Country byr = createCountry("BY", "Belarus", 9056696L);
        Country bg = createCountry("BG", "Bulgaria", 6757689L);
        Country rs = createCountry("RS", "Serbia", 6736216L);
        Country dk = createCountry("DK", "Denmark", 5977412L);
        Country fi = createCountry("FI", "Finland", 5617310L);
        Country sk = createCountry("SK", "Slovakia", 5506760L);
        Country ie = createCountry("IE", "Ireland", 5255017L);
        Country hr = createCountry("HR", "Croatia", 3875325L);
        Country ge = createCountry("GE", "Georgia", 3807670L);
        Country ba = createCountry("BA", "Bosnia and Herzegovina", 3164253L);
        Country md = createCountry("MD", "Moldova", 3034961L);
        Country lt = createCountry("LT", "Lithuania", 2859110L);
        Country al = createCountry("AL", "Albania", 2791765L);
        Country si = createCountry("SI", "Slovenia", 2118697L);
        Country lv = createCountry("LV", "Latvia", 1871871L);
        Country mk = createCountry("MK", "North Macedonia", 1823009L);
        Country xk = createCountry("XK", "Kosovo", 1684790L);
        Country ee = createCountry("EE", "Estonia", 1360546L);
        Country cy = createCountry("CY", "Cyprus", 1358282L);
        Country lu = createCountry("LU", "Luxembourg", 673036L);
        Country me = createCountry("ME", "Montenegro", 638479L);
        Country mt = createCountry("MT", "Malta", 539607L);
        Country is = createCountry("IS", "Iceland", 393396L);
        Country ad = createCountry("AD", "Andorra", 81938L);
        Country li = createCountry("LI", "Liechtenstein", 39870L);
        Country mc = createCountry("MC", "Monaco", 38631L);
        Country sm = createCountry("SM", "San Marino", 33581L);
        Country va = createCountry("VA", "Vatican City", 496L);

        // Load German regions
        Region bw = createRegion("DE-BW", "Baden-Württemberg", de, 11100000L);
        Region by = createRegion("DE-BY", "Bayern", de, 13100000L);
        Region be = createRegion("DE-BE", "Berlin", de, 3650000L);
        Region bb = createRegion("DE-BB", "Brandenburg", de, 2520000L);
        Region hb = createRegion("DE-HB", "Bremen", de, 680000L);
        Region hh = createRegion("DE-HH", "Hamburg", de, 1850000L);
        Region he = createRegion("DE-HE", "Hessen", de, 6290000L);
        Region mv = createRegion("DE-MV", "Mecklenburg-Vorpommern", de, 1610000L);
        Region ni = createRegion("DE-NI", "Niedersachsen", de, 8000000L);
        Region nw = createRegion("DE-NW", "Nordrhein-Westfalen", de, 17930000L);
        Region rp = createRegion("DE-RP", "Rheinland-Pfalz", de, 4090000L);
        Region sl = createRegion("DE-SL", "Saarland", de, 990000L);
        Region sn = createRegion("DE-SN", "Sachsen", de, 4080000L);
        Region st = createRegion("DE-ST", "Sachsen-Anhalt", de, 2190000L);
        Region sh = createRegion("DE-SH", "Schleswig-Holstein", de, 2910000L);
        Region th = createRegion("DE-TH", "Thüringen", de, 2120000L);

        // Load Austrian regions
        Region at1 = createRegion("AT-1", "Burgenland", at, 294000L);
        Region at2 = createRegion("AT-2", "Kärnten", at, 561000L);
        Region at3 = createRegion("AT-3", "Niederösterreich", at, 1690000L);
        Region at4 = createRegion("AT-4", "Oberösterreich", at, 1490000L);
        Region at5 = createRegion("AT-5", "Salzburg", at, 560000L);
        Region at6 = createRegion("AT-6", "Steiermark", at, 1250000L);
        Region at7 = createRegion("AT-7", "Tirol", at, 760000L);
        Region at8 = createRegion("AT-8", "Vorarlberg", at, 400000L);
        Region at9 = createRegion("AT-9", "Wien", at, 1900000L);

        // Load Swiss regions
        createRegion("CH-AG", "Aargau", ch, 690000L);
        createRegion("CH-AI", "Appenzell Innerrhoden", ch, 16000L);
        createRegion("CH-AR", "Appenzell Ausserrhoden", ch, 55000L);
        createRegion("CH-BE", "Bern", ch, 1040000L);
        createRegion("CH-BL", "Basel-Landschaft", ch, 290000L);
        createRegion("CH-BS", "Basel-Stadt", ch, 195000L);
        createRegion("CH-FR", "Fribourg", ch, 320000L);
        createRegion("CH-GE", "Genève", ch, 500000L);
        createRegion("CH-GL", "Glarus", ch, 41000L);
        createRegion("CH-GR", "Graubünden", ch, 200000L);
        createRegion("CH-JU", "Jura", ch, 73000L);
        createRegion("CH-LU", "Luzern", ch, 410000L);
        createRegion("CH-NE", "Neuchâtel", ch, 177000L);
        createRegion("CH-NW", "Nidwalden", ch, 43000L);
        createRegion("CH-OW", "Obwalden", ch, 38000L);
        createRegion("CH-SG", "St. Gallen", ch, 510000L);
        createRegion("CH-SH", "Schaffhausen", ch, 83000L);
        createRegion("CH-SO", "Solothurn", ch, 276000L);
        createRegion("CH-SZ", "Schwyz", ch, 160000L);
        createRegion("CH-TG", "Thurgau", ch, 280000L);
        createRegion("CH-TI", "Ticino", ch, 350000L);
        createRegion("CH-UR", "Uri", ch, 37000L);
        createRegion("CH-VD", "Vaud", ch, 810000L);
        createRegion("CH-VS", "Valais", ch, 350000L);
        createRegion("CH-ZG", "Zug", ch, 130000L);
        createRegion("CH-ZH", "Zürich", ch, 1540000L);

        // Load French regions
        createRegion("FR-ARA", "Auvergne-Rhône-Alpes", fr, 8040000L);
        createRegion("FR-BFC", "Bourgogne-Franche-Comté", fr, 2800000L);
        createRegion("FR-BRE", "Bretagne", fr, 3340000L);
        createRegion("FR-CVL", "Centre-Val de Loire", fr, 2570000L);
        createRegion("FR-COR", "Corse", fr, 340000L);
        createRegion("FR-GES", "Grand Est", fr, 5560000L);
        createRegion("FR-HDF", "Hauts-de-France", fr, 6000000L);
        createRegion("FR-IDF", "Île-de-France", fr, 12270000L);
        createRegion("FR-NOR", "Normandie", fr, 3330000L);
        createRegion("FR-NAQ", "Nouvelle-Aquitaine", fr, 6000000L);
        createRegion("FR-OCC", "Occitanie", fr, 5920000L);
        createRegion("FR-PDL", "Pays de la Loire", fr, 3800000L);
        createRegion("FR-PAC", "Provence-Alpes-Côte d'Azur", fr, 5050000L);

        // Load Spanish regions
        Region es1 = createRegion("ES-AN", "Andalucía", es, 8470000L);
        Region es2 = createRegion("ES-AR", "Aragón", es, 1320000L);
        Region es3 = createRegion("ES-AS", "Asturias", es, 1020000L);
        Region es4 = createRegion("ES-IB", "Islas Baleares", es, 1170000L);
        Region es5 = createRegion("ES-CN", "Islas Canarias", es, 2170000L);
        Region es6 = createRegion("ES-CB", "Cantabria", es, 580000L);
        Region es7 = createRegion("ES-CL", "Castilla y León", es, 2400000L);
        Region es8 = createRegion("ES-CM", "Castilla-La Mancha", es, 2040000L);
        Region es9 = createRegion("ES-CT", "Cataluña", es, 7670000L);
        Region es10 = createRegion("ES-EX", "Extremadura", es, 1070000L);
        Region es11 = createRegion("ES-GA", "Galicia", es, 2700000L);
        Region es12 = createRegion("ES-MD", "Madrid", es, 6750000L);
        Region es13 = createRegion("ES-MC", "Murcia", es, 1510000L);
        Region es14 = createRegion("ES-NC", "Navarra", es, 660000L);
        Region es15 = createRegion("ES-PV", "País Vasco", es, 2210000L);
        Region es16 = createRegion("ES-RI", "La Rioja", es, 320000L);
        Region es17 = createRegion("ES-VC", "Comunidad Valenciana", es, 5060000L);

        // Load Dutch regions
        Region nlDR = createRegion("NL-DR", "Drenthe", nl, 493000L);
        Region nlFL = createRegion("NL-FL", "Flevoland", nl, 430000L);
        Region nlFR = createRegion("NL-FR", "Friesland", nl, 650000L);
        Region nlGE = createRegion("NL-GE", "Gelderland", nl, 2090000L);
        Region nlGR = createRegion("NL-GR", "Groningen", nl, 585000L);
        Region nlLI = createRegion("NL-LI", "Limburg", nl, 1120000L);
        Region nlNB = createRegion("NL-NB", "Noord-Brabant", nl, 2570000L);
        Region nlNH = createRegion("NL-NH", "Noord-Holland", nl, 2880000L);
        Region nlOV = createRegion("NL-OV", "Overijssel", nl, 1160000L);
        Region nlUT = createRegion("NL-UT", "Utrecht", nl, 1360000L);
        Region nlZE = createRegion("NL-ZE", "Zeeland", nl, 385000L);
        Region nlZH = createRegion("NL-ZH", "Zuid-Holland", nl, 3710000L);


        /// Load Italian regions
        Region it1 = createRegion("IT-65", "Abruzzo", it, 1268430L);
        Region it2 = createRegion("IT-77", "Basilicata", it, 529897L);
        Region it3 = createRegion("IT-78", "Calabria", it, 1832147L);
        Region it4 = createRegion("IT-72", "Campania", it, 5575025L);
        Region it5 = createRegion("IT-45", "Emilia-Romagna", it, 4465678L);
        Region it6 = createRegion("IT-36", "Friuli-Venezia Giulia", it, 1194095L);
        Region it7 = createRegion("IT-62", "Lazio", it, 5710272L);
        Region it8 = createRegion("IT-42", "Liguria", it, 1509908L);
        Region it9 = createRegion("IT-25", "Lombardia", it, 10035481L);
        Region it10 = createRegion("IT-57", "Marche", it, 1481252L);
        Region it11 = createRegion("IT-67", "Molise", it, 287966L);
        Region it12 = createRegion("IT-21", "Piemonte", it, 4255702L);
        Region it13 = createRegion("IT-75", "Puglia", it, 3874166L);
        Region it14 = createRegion("IT-88", "Sardegna", it, 1561339L);
        Region it15 = createRegion("IT-82", "Sicilia", it, 4779371L);
        Region it16 = createRegion("IT-52", "Toscana", it, 3660834L);
        Region it17 = createRegion("IT-32", "Trentino-Alto Adige", it, 1086095L);
        Region it18 = createRegion("IT-55", "Umbria", it, 851954L);
        Region it19 = createRegion("IT-23", "Valle d'Aosta", it, 122714L);
        Region it20 = createRegion("IT-34", "Veneto", it, 4851851L);

        // Load Polish regions
        Region pl1 = createRegion("PL-1", "Ermland-Masuren", pl, 1422737L);
        Region pl2 = createRegion("PL-2", "Großpolen", pl, 3498733L);
        Region pl3 = createRegion("PL-3", "Heiligkreuz", pl, 1233961L);
        Region pl4 = createRegion("PL-4", "Karpatenvorland", pl, 2127164L);
        Region pl5 = createRegion("PL-5", "Kleinpolen", pl, 3410901L);
        Region pl6 = createRegion("PL-6", "Kujawien-Pommern", pl, 2072373L);
        Region pl7 = createRegion("PL-7", "Lebus", pl, 1011592L);
        Region pl8 = createRegion("PL-8", "Lodsch", pl, 2454779L);
        Region pl9 = createRegion("PL-9", "Lublin", pl, 2108270L);
        Region pl10 = createRegion("PL-10", "Masowien", pl, 5423168L);
        Region pl11 = createRegion("PL-11", "Niederschlesien", pl, 2900163L);
        Region pl12 = createRegion("PL-12", "Oppeln", pl, 982626L);
        Region pl13 = createRegion("PL-13", "Podlachien", pl, 1178353L);
        Region pl14 = createRegion("PL-14", "Pommern", pl, 2343928L);
        Region pl15 = createRegion("PL-15", "Schlesien", pl, 4517635L);
        Region pl16 = createRegion("PL-16", "Westpommern", pl, 1696193L);

        // Load Norwegian regions
        Region no1 = createRegion("NO-1", "Agder", no, 322188L);
        Region no2 = createRegion("NO-2", "Akershus", no, 740680L);
        Region no3 = createRegion("NO-3", "Buskerud", no, 271248L);
        Region no4 = createRegion("NO-4", "Finnmark", no, 75042L);
        Region no5 = createRegion("NO-5", "Innlandet", no, 377556L);
        Region no6 = createRegion("NO-6", "Møre og Romsdal", no, 272413L);
        Region no7 = createRegion("NO-7", "Nordland", no, 243582L);
        Region no8 = createRegion("NO-8", "Oslo", no, 724290L);
        Region no9 = createRegion("NO-9", "Østfold", no, 314407L);
        Region no10 = createRegion("NO-10", "Rogaland", no, 504496L);
        Region no11 = createRegion("NO-11", "Telemark", no, 177863L);
        Region no12 = createRegion("NO-12", "Trøndelag", no, 486815L);
        Region no13 = createRegion("NO-13", "Troms", no, 170479L);
        Region no14 = createRegion("NO-14", "Vestfold", no, 258071L);
        Region no15 = createRegion("NO-15", "Vestland", no, 655210L);

        // Load German School Holidays 2025 (from schulferien.org)
        // Baden-Württemberg
        createSchoolHoliday("Osterferien", bw, "2025-04-14", "2025-04-26", 2025);
        createSchoolHoliday("Pfingstferien", bw, "2025-06-10", "2025-06-20", 2025);
        createSchoolHoliday("Sommerferien", bw, "2025-07-31", "2025-09-13", 2025);
        createSchoolHoliday("Herbstferien", bw, "2025-10-27", "2025-10-31", 2025);
        createSchoolHoliday("Weihnachtsferien", bw, "2025-12-22", "2026-01-05", 2025);

        // Bayern
        createSchoolHoliday("Winterferien", by, "2025-03-03", "2025-03-07", 2025);
        createSchoolHoliday("Osterferien", by, "2025-04-14", "2025-04-25", 2025);
        createSchoolHoliday("Pfingstferien", by, "2025-06-10", "2025-06-20", 2025);
        createSchoolHoliday("Sommerferien", by, "2025-08-01", "2025-09-15", 2025);
        createSchoolHoliday("Herbstferien", by, "2025-11-03", "2025-11-07", 2025);
        createSchoolHoliday("Weihnachtsferien", by, "2025-12-22", "2026-01-05", 2025);

        // Berlin
        createSchoolHoliday("Winterferien", be, "2025-02-03", "2025-02-08", 2025);
        createSchoolHoliday("Osterferien", be, "2025-04-14", "2025-04-25", 2025);
        createSchoolHoliday("Pfingstferien", be, "2025-06-10", "2025-06-10", 2025);
        createSchoolHoliday("Sommerferien", be, "2025-07-24", "2025-09-06", 2025);
        createSchoolHoliday("Herbstferien", be, "2025-10-20", "2025-11-01", 2025);
        createSchoolHoliday("Weihnachtsferien", be, "2025-12-22", "2026-01-02", 2025);

        // Brandenburg
        createSchoolHoliday("Winterferien", bb, "2025-02-03", "2025-02-08", 2025);
        createSchoolHoliday("Osterferien", bb, "2025-04-14", "2025-04-25", 2025);
        createSchoolHoliday("Pfingstferien", bb, "2025-06-10", "2025-06-10", 2025);
        createSchoolHoliday("Sommerferien", bb, "2025-07-24", "2025-09-06", 2025);
        createSchoolHoliday("Herbstferien", bb, "2025-10-20", "2025-11-01", 2025);
        createSchoolHoliday("Weihnachtsferien", bb, "2025-12-22", "2026-01-02", 2025);

        // Bremen
        createSchoolHoliday("Winterferien", hb, "2025-02-03", "2025-02-04", 2025);
        createSchoolHoliday("Osterferien", hb, "2025-04-07", "2025-04-19", 2025);
        createSchoolHoliday("Sommerferien", hb, "2025-07-03", "2025-08-13", 2025);
        createSchoolHoliday("Herbstferien", hb, "2025-10-13", "2025-10-25", 2025);
        createSchoolHoliday("Weihnachtsferien", hb, "2025-12-22", "2026-01-05", 2025);

        // Hamburg
        createSchoolHoliday("Winterferien", hh, "2025-01-31", "2025-01-31", 2025);
        createSchoolHoliday("Osterferien", hh, "2025-03-10", "2025-03-21", 2025);
        createSchoolHoliday("Pfingstferien", hh, "2025-05-02", "2025-05-30", 2025);
        createSchoolHoliday("Sommerferien", hh, "2025-07-24", "2025-09-03", 2025);
        createSchoolHoliday("Herbstferien", hh, "2025-10-20", "2025-10-31", 2025);
        createSchoolHoliday("Weihnachtsferien", hh, "2025-12-17", "2026-01-02", 2025);

        // Hessen
        createSchoolHoliday("Osterferien", he, "2025-04-07", "2025-04-21", 2025);
        createSchoolHoliday("Sommerferien", he, "2025-07-07", "2025-08-15", 2025);
        createSchoolHoliday("Herbstferien", he, "2025-10-06", "2025-10-18", 2025);
        createSchoolHoliday("Weihnachtsferien", he, "2025-12-22", "2026-01-10", 2025);

        // Mecklenburg-Vorpommern
        createSchoolHoliday("Winterferien", mv, "2025-02-03", "2025-02-14", 2025);
        createSchoolHoliday("Osterferien", mv, "2025-04-14", "2025-04-23", 2025);
        createSchoolHoliday("Pfingstferien", mv, "2025-06-06", "2025-06-10", 2025);
        createSchoolHoliday("Sommerferien", mv, "2025-07-28", "2025-09-06", 2025);
        createSchoolHoliday("Herbstferien", mv, "2025-10-20", "2025-10-25", 2025);
        createSchoolHoliday("Weihnachtsferien", mv, "2025-12-20", "2026-01-03", 2025);

        // Niedersachsen
        createSchoolHoliday("Winterferien", ni, "2025-02-03", "2025-02-04", 2025);
        createSchoolHoliday("Osterferien", ni, "2025-04-07", "2025-04-19", 2025);
        createSchoolHoliday("Sommerferien", ni, "2025-07-03", "2025-08-13", 2025);
        createSchoolHoliday("Herbstferien", ni, "2025-10-13", "2025-10-25", 2025);
        createSchoolHoliday("Weihnachtsferien", ni, "2025-12-22", "2026-01-05", 2025);

        // Nordrhein-Westfalen
        createSchoolHoliday("Osterferien", nw, "2025-04-14", "2025-04-26", 2025);
        createSchoolHoliday("Pfingstferien", nw, "2025-06-10", "2025-06-10", 2025);
        createSchoolHoliday("Sommerferien", nw, "2025-07-14", "2025-08-26", 2025);
        createSchoolHoliday("Herbstferien", nw, "2025-10-13", "2025-10-25", 2025);
        createSchoolHoliday("Weihnachtsferien", nw, "2025-12-22", "2026-01-06", 2025);

        // Rheinland-Pfalz
        createSchoolHoliday("Osterferien", rp, "2025-04-14", "2025-04-25", 2025);
        createSchoolHoliday("Sommerferien", rp, "2025-07-07", "2025-08-15", 2025);
        createSchoolHoliday("Herbstferien", rp, "2025-10-13", "2025-10-24", 2025);
        createSchoolHoliday("Weihnachtsferien", rp, "2025-12-22", "2026-01-07", 2025);

        // Saarland
        createSchoolHoliday("Winterferien", sl, "2025-02-24", "2025-03-04", 2025);
        createSchoolHoliday("Osterferien", sl, "2025-04-14", "2025-04-25", 2025);
        createSchoolHoliday("Sommerferien", sl, "2025-07-07", "2025-08-14", 2025);
        createSchoolHoliday("Herbstferien", sl, "2025-10-13", "2025-10-24", 2025);
        createSchoolHoliday("Weihnachtsferien", sl, "2025-12-22", "2026-01-02", 2025);

        // Sachsen
        createSchoolHoliday("Winterferien", sn, "2025-02-17", "2025-03-01", 2025);
        createSchoolHoliday("Osterferien", sn, "2025-04-18", "2025-04-25", 2025);
        createSchoolHoliday("Sommerferien", sn, "2025-06-28", "2025-08-08", 2025);
        createSchoolHoliday("Herbstferien", sn, "2025-10-06", "2025-10-18", 2025);
        createSchoolHoliday("Weihnachtsferien", sn, "2025-12-22", "2026-01-02", 2025);

        // Sachsen-Anhalt
        createSchoolHoliday("Winterferien", st, "2025-01-27", "2025-01-31", 2025);
        createSchoolHoliday("Osterferien", st, "2025-04-07", "2025-04-19", 2025);
        createSchoolHoliday("Pfingstferien", st, "2025-05-30", "2025-05-30", 2025);
        createSchoolHoliday("Sommerferien", st, "2025-06-28", "2025-08-08", 2025);
        createSchoolHoliday("Herbstferien", st, "2025-10-13", "2025-10-25", 2025);
        createSchoolHoliday("Weihnachtsferien", st, "2025-12-22", "2026-01-05", 2025);

        // Schleswig-Holstein
        createSchoolHoliday("Osterferien", sh, "2025-04-11", "2025-04-25", 2025);
        createSchoolHoliday("Pfingstferien", sh, "2025-05-30", "2025-05-30", 2025);
        createSchoolHoliday("Sommerferien", sh, "2025-07-28", "2025-09-06", 2025);
        createSchoolHoliday("Herbstferien", sh, "2025-10-20", "2025-10-30", 2025);
        createSchoolHoliday("Weihnachtsferien", sh, "2025-12-19", "2026-01-06", 2025);

        // Thüringen
        createSchoolHoliday("Winterferien", th, "2025-02-03", "2025-02-08", 2025);
        createSchoolHoliday("Osterferien", th, "2025-04-07", "2025-04-19", 2025);
        createSchoolHoliday("Pfingstferien", th, "2025-05-30", "2025-05-30", 2025);
        createSchoolHoliday("Sommerferien", th, "2025-06-28", "2025-08-08", 2025);
        createSchoolHoliday("Herbstferien", th, "2025-10-06", "2025-10-18", 2025);
        createSchoolHoliday("Weihnachtsferien", th, "2025-12-22", "2026-01-03", 2025);

        // Load German School Holidays 2026
        // Baden-Württemberg
        createSchoolHoliday("Osterferien", bw, "2026-04-14", "2026-04-26", 2026);
        createSchoolHoliday("Pfingstferien", bw, "2026-06-10", "2026-06-20", 2026);
        createSchoolHoliday("Sommerferien", bw, "2026-07-31", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", bw, "2026-10-27", "2026-10-31", 2026);
        createSchoolHoliday("Weihnachtsferien", bw, "2026-12-22", "2027-01-05", 2026);

        // Bayern
        createSchoolHoliday("Winterferien", by, "2026-03-03", "2026-03-07", 2026);
        createSchoolHoliday("Osterferien", by, "2026-04-14", "2026-04-25", 2026);
        createSchoolHoliday("Pfingstferien", by, "2026-06-10", "2026-06-20", 2026);
        createSchoolHoliday("Sommerferien", by, "2026-08-01", "2026-09-15", 2026);
        createSchoolHoliday("Herbstferien", by, "2026-11-03", "2026-11-07", 2026);
        createSchoolHoliday("Weihnachtsferien", by, "2026-12-22", "2027-01-05", 2026);

        // Berlin
        createSchoolHoliday("Winterferien", be, "2026-02-03", "2026-02-08", 2026);
        createSchoolHoliday("Osterferien", be, "2026-04-14", "2026-04-25", 2026);
        createSchoolHoliday("Pfingstferien", be, "2026-06-10", "2026-06-10", 2026);
        createSchoolHoliday("Sommerferien", be, "2026-07-24", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", be, "2026-10-20", "2026-11-01", 2026);
        createSchoolHoliday("Weihnachtsferien", be, "2026-12-22", "2027-01-02", 2026);

        // Brandenburg
        createSchoolHoliday("Winterferien", bb, "2026-02-03", "2026-02-08", 2026);
        createSchoolHoliday("Osterferien", bb, "2026-04-14", "2026-04-25", 2026);
        createSchoolHoliday("Pfingstferien", bb, "2026-06-10", "2026-06-10", 2026);
        createSchoolHoliday("Sommerferien", bb, "2026-07-24", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", bb, "2026-10-20", "2026-11-01", 2026);
        createSchoolHoliday("Weihnachtsferien", bb, "2026-12-22", "2027-01-02", 2026);

        // Bremen
        createSchoolHoliday("Winterferien", hb, "2026-02-03", "2026-02-04", 2026);
        createSchoolHoliday("Osterferien", hb, "2026-04-07", "2026-04-19", 2026);
        createSchoolHoliday("Sommerferien", hb, "2026-07-03", "2026-08-13", 2026);
        createSchoolHoliday("Herbstferien", hb, "2026-10-13", "2026-10-25", 2026);
        createSchoolHoliday("Weihnachtsferien", hb, "2026-12-22", "2027-01-05", 2026);

        // Hamburg
        createSchoolHoliday("Winterferien", hh, "2026-01-31", "2026-01-31", 2026);
        createSchoolHoliday("Osterferien", hh, "2026-03-10", "2026-03-21", 2026);
        createSchoolHoliday("Pfingstferien", hh, "2026-05-02", "2026-05-30", 2026);
        createSchoolHoliday("Sommerferien", hh, "2026-07-24", "2026-09-03", 2026);
        createSchoolHoliday("Herbstferien", hh, "2026-10-20", "2026-10-31", 2026);
        createSchoolHoliday("Weihnachtsferien", hh, "2026-12-17", "2027-01-02", 2026);

        // Hessen
        createSchoolHoliday("Osterferien", he, "2026-04-07", "2026-04-21", 2026);
        createSchoolHoliday("Sommerferien", he, "2026-07-07", "2026-08-15", 2026);
        createSchoolHoliday("Herbstferien", he, "2026-10-06", "2026-10-18", 2026);
        createSchoolHoliday("Weihnachtsferien", he, "2026-12-22", "2027-01-10", 2026);

        // Mecklenburg-Vorpommern
        createSchoolHoliday("Winterferien", mv, "2026-02-03", "2026-02-14", 2026);
        createSchoolHoliday("Osterferien", mv, "2026-04-14", "2026-04-23", 2026);
        createSchoolHoliday("Pfingstferien", mv, "2026-06-06", "2026-06-10", 2026);
        createSchoolHoliday("Sommerferien", mv, "2026-07-28", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", mv, "2026-10-20", "2026-10-25", 2026);
        createSchoolHoliday("Weihnachtsferien", mv, "2026-12-20", "2027-01-03", 2026);

        // Niedersachsen
        createSchoolHoliday("Winterferien", ni, "2026-02-03", "2026-02-04", 2026);
        createSchoolHoliday("Osterferien", ni, "2026-04-07", "2026-04-19", 2026);
        createSchoolHoliday("Sommerferien", ni, "2026-07-03", "2026-08-13", 2026);
        createSchoolHoliday("Herbstferien", ni, "2026-10-13", "2026-10-25", 2026);
        createSchoolHoliday("Weihnachtsferien", ni, "2026-12-22", "2027-01-05", 2026);

        // Nordrhein-Westfalen
        createSchoolHoliday("Osterferien", nw, "2026-04-14", "2026-04-26", 2026);
        createSchoolHoliday("Pfingstferien", nw, "2026-06-10", "2026-06-10", 2026);
        createSchoolHoliday("Sommerferien", nw, "2026-07-14", "2026-08-26", 2026);
        createSchoolHoliday("Herbstferien", nw, "2026-10-13", "2026-10-25", 2026);
        createSchoolHoliday("Weihnachtsferien", nw, "2026-12-22", "2027-01-06", 2026);

        // Rheinland-Pfalz
        createSchoolHoliday("Osterferien", rp, "2026-04-14", "2026-04-25", 2026);
        createSchoolHoliday("Sommerferien", rp, "2026-07-07", "2026-08-15", 2026);
        createSchoolHoliday("Herbstferien", rp, "2026-10-13", "2026-10-24", 2026);
        createSchoolHoliday("Weihnachtsferien", rp, "2026-12-22", "2027-01-07", 2026);

        // Saarland
        createSchoolHoliday("Winterferien", sl, "2026-02-24", "2026-03-04", 2026);
        createSchoolHoliday("Osterferien", sl, "2026-04-14", "2026-04-25", 2026);
        createSchoolHoliday("Sommerferien", sl, "2026-07-07", "2026-08-14", 2026);
        createSchoolHoliday("Herbstferien", sl, "2026-10-13", "2026-10-24", 2026);
        createSchoolHoliday("Weihnachtsferien", sl, "2026-12-22", "2027-01-02", 2026);

        // Sachsen
        createSchoolHoliday("Winterferien", sn, "2026-02-17", "2026-03-01", 2026);
        createSchoolHoliday("Osterferien", sn, "2026-04-18", "2026-04-25", 2026);
        createSchoolHoliday("Sommerferien", sn, "2026-06-28", "2026-08-08", 2026);
        createSchoolHoliday("Herbstferien", sn, "2026-10-06", "2026-10-18", 2026);
        createSchoolHoliday("Weihnachtsferien", sn, "2026-12-22", "2027-01-02", 2026);

        // Sachsen-Anhalt
        createSchoolHoliday("Winterferien", st, "2026-01-27", "2026-01-31", 2026);
        createSchoolHoliday("Osterferien", st, "2026-04-07", "2026-04-19", 2026);
        createSchoolHoliday("Pfingstferien", st, "2026-05-30", "2026-05-30", 2026);
        createSchoolHoliday("Sommerferien", st, "2026-06-28", "2026-08-08", 2026);
        createSchoolHoliday("Herbstferien", st, "2026-10-13", "2026-10-25", 2026);
        createSchoolHoliday("Weihnachtsferien", st, "2026-12-22", "2027-01-05", 2026);

        // Schleswig-Holstein
        createSchoolHoliday("Osterferien", sh, "2026-04-11", "2026-04-25", 2026);
        createSchoolHoliday("Pfingstferien", sh, "2026-05-30", "2026-05-30", 2026);
        createSchoolHoliday("Sommerferien", sh, "2026-07-28", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", sh, "2026-10-20", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", sh, "2026-12-19", "2027-01-06", 2026);

        // Thüringen
        createSchoolHoliday("Winterferien", th, "2026-02-03", "2026-02-08", 2026);
        createSchoolHoliday("Osterferien", th, "2026-04-07", "2026-04-19", 2026);
        createSchoolHoliday("Pfingstferien", th, "2026-05-30", "2026-05-30", 2026);
        createSchoolHoliday("Sommerferien", th, "2026-06-28", "2026-08-08", 2026);
        createSchoolHoliday("Herbstferien", th, "2026-10-06", "2026-10-18", 2026);
        createSchoolHoliday("Weihnachtsferien", th, "2026-12-22", "2027-01-03", 2026);


        // Load Austrich School Holidays 2026
        // Austrian School Holidays 2026
// Burgenland
        createSchoolHoliday("Semesterferien", at1, "2026-02-09", "2026-02-15", 2026);
        createSchoolHoliday("Osterferien", at1, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at1, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at1, "2026-07-04", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", at1, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Herbstferien", at1, "2026-11-11", "2026-11-11", 2026);
        createSchoolHoliday("Weihnachtsferien", at1, "2026-12-24", "2027-01-06", 2026);

// Kärnten
        createSchoolHoliday("Semesterferien", at2, "2026-02-09", "2026-02-15", 2026);
        createSchoolHoliday("Osterferien", at2, "2026-03-19", "2026-03-28", 2026);
        createSchoolHoliday("Osterferien", at2, "2026-04-06", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at2, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at2, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at2, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at2, "2026-12-24", "2027-01-06", 2026);

// Niederösterreich
        createSchoolHoliday("Semesterferien", at3, "2026-02-02", "2026-02-08", 2026);
        createSchoolHoliday("Osterferien", at3, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at3, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at3, "2026-07-04", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", at3, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Herbstferien", at3, "2026-11-15", "2026-11-15", 2026);
        createSchoolHoliday("Weihnachtsferien", at3, "2026-12-24", "2027-01-06", 2026);

// Oberösterreich
        createSchoolHoliday("Semesterferien", at4, "2026-02-16", "2026-02-22", 2026);
        createSchoolHoliday("Osterferien", at4, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at4, "2026-05-04", "2026-05-23", 2026);
        createSchoolHoliday("Pfingstferien", at4, "2026-05-25", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at4, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at4, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at4, "2026-12-24", "2027-01-06", 2026);

// Salzburg
        createSchoolHoliday("Semesterferien", at5, "2026-02-09", "2026-02-15", 2026);
        createSchoolHoliday("Osterferien", at5, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at5, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at5, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at5, "2026-09-24", "2026-10-27", 2026);
        createSchoolHoliday("Herbstferien", at5, "2026-10-30", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at5, "2026-12-24", "2027-01-06", 2026);

// Steiermark
        createSchoolHoliday("Semesterferien", at6, "2026-02-16", "2026-02-22", 2026);
        createSchoolHoliday("Osterferien", at6, "2026-03-19", "2026-03-28", 2026);
        createSchoolHoliday("Osterferien", at6, "2026-04-06", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at6, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at6, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at6, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at6, "2026-12-24", "2027-01-06", 2026);

// Tirol
        createSchoolHoliday("Semesterferien", at7, "2026-02-09", "2026-02-15", 2026);
        createSchoolHoliday("Osterferien", at7, "2026-03-19", "2026-03-28", 2026);
        createSchoolHoliday("Osterferien", at7, "2026-04-06", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at7, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at7, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at7, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at7, "2026-12-24", "2027-01-06", 2026);

// Vorarlberg
        createSchoolHoliday("Semesterferien", at8, "2026-02-09", "2026-02-15", 2026);
        createSchoolHoliday("Osterferien", at8, "2026-03-19", "2026-03-28", 2026);
        createSchoolHoliday("Osterferien", at8, "2026-04-06", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at8, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at8, "2026-07-11", "2026-09-13", 2026);
        createSchoolHoliday("Herbstferien", at8, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Weihnachtsferien", at8, "2026-12-24", "2027-01-06", 2026);

// Wien
        createSchoolHoliday("Semesterferien", at9, "2026-02-02", "2026-02-08", 2026);
        createSchoolHoliday("Osterferien", at9, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Pfingstferien", at9, "2026-05-23", "2026-05-25", 2026);
        createSchoolHoliday("Sommerferien", at9, "2026-07-04", "2026-09-06", 2026);
        createSchoolHoliday("Herbstferien", at9, "2026-10-27", "2026-10-30", 2026);
        createSchoolHoliday("Herbstferien", at9, "2026-11-15", "2026-11-15", 2026);
        createSchoolHoliday("Weihnachtsferien", at9, "2026-12-24", "2027-01-06", 2026);



        // Dutch School Holidays 2026
// Nord-Regionen (Frühlingsferien 21.02-01.03, Sommerferien 04.07-16.08)
// Drenthe
        createSchoolHoliday("Frühlingsferien", nlDR, "2026-02-21", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlDR, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlDR, "2026-07-04", "2026-08-16", 2026);

// Flevoland
        createSchoolHoliday("Frühlingsferien", nlFL, "2026-02-14", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlFL, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlFL, "2026-07-04", "2026-08-30", 2026);

// Friesland
        createSchoolHoliday("Frühlingsferien", nlFR, "2026-02-21", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlFR, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlFR, "2026-07-04", "2026-08-16", 2026);

// Gelderland
        createSchoolHoliday("Frühlingsferien", nlGE, "2026-02-14", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlGE, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlGE, "2026-07-04", "2026-08-30", 2026);

// Groningen
        createSchoolHoliday("Frühlingsferien", nlGR, "2026-02-21", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlGR, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlGR, "2026-07-04", "2026-08-16", 2026);

// Limburg
        createSchoolHoliday("Frühlingsferien", nlLI, "2026-02-14", "2026-02-22", 2026);
        createSchoolHoliday("Maiferien", nlLI, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlLI, "2026-07-11", "2026-08-23", 2026);

// Noord-Brabant
        createSchoolHoliday("Frühlingsferien", nlNB, "2026-02-14", "2026-02-22", 2026);
        createSchoolHoliday("Maiferien", nlNB, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlNB, "2026-07-11", "2026-08-30", 2026);

// Noord-Holland
        createSchoolHoliday("Frühlingsferien", nlNH, "2026-02-21", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlNH, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlNH, "2026-07-04", "2026-08-16", 2026);

// Overijssel
        createSchoolHoliday("Frühlingsferien", nlOV, "2026-02-21", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlOV, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlOV, "2026-07-04", "2026-08-16", 2026);

// Utrecht
        createSchoolHoliday("Frühlingsferien", nlUT, "2026-02-14", "2026-03-01", 2026);
        createSchoolHoliday("Maiferien", nlUT, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlUT, "2026-07-04", "2026-08-30", 2026);

// Zeeland
        createSchoolHoliday("Frühlingsferien", nlZE, "2026-02-14", "2026-02-22", 2026);
        createSchoolHoliday("Maiferien", nlZE, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlZE, "2026-07-11", "2026-08-23", 2026);

// Zuid-Holland
        createSchoolHoliday("Frühlingsferien", nlZH, "2026-02-14", "2026-02-22", 2026);
        createSchoolHoliday("Maiferien", nlZH, "2026-04-25", "2026-05-03", 2026);
        createSchoolHoliday("Sommerferien", nlZH, "2026-07-18", "2026-08-30", 2026);


        // Polish School Holidays 2026
// Ermland-Masuren
        createSchoolHoliday("Frühjahrsferien", pl1, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl1, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl1, "2026-12-23", "2026-12-31", 2026);

// Großpolen
        createSchoolHoliday("Frühjahrsferien", pl2, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl2, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl2, "2026-12-23", "2026-12-31", 2026);

// Heiligkreuz
        createSchoolHoliday("Frühjahrsferien", pl3, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl3, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl3, "2026-12-23", "2026-12-31", 2026);

// Karpatenvorland
        createSchoolHoliday("Frühjahrsferien", pl4, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl4, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl4, "2026-12-23", "2026-12-31", 2026);

// Kleinpolen
        createSchoolHoliday("Frühjahrsferien", pl5, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl5, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl5, "2026-12-23", "2026-12-31", 2026);

// Kujawien-Pommern
        createSchoolHoliday("Frühjahrsferien", pl6, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl6, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl6, "2026-12-23", "2026-12-31", 2026);

// Lebus
        createSchoolHoliday("Frühjahrsferien", pl7, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl7, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl7, "2026-12-23", "2026-12-31", 2026);

// Lodsch
        createSchoolHoliday("Frühjahrsferien", pl8, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl8, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl8, "2026-12-23", "2026-12-31", 2026);

// Lublin
        createSchoolHoliday("Frühjahrsferien", pl9, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl9, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl9, "2026-12-23", "2026-12-31", 2026);

// Masowien
        createSchoolHoliday("Frühjahrsferien", pl10, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl10, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl10, "2026-12-23", "2026-12-31", 2026);

// Niederschlesien
        createSchoolHoliday("Frühjahrsferien", pl11, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl11, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl11, "2026-12-23", "2026-12-31", 2026);

// Oppeln
        createSchoolHoliday("Frühjahrsferien", pl12, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl12, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl12, "2026-12-23", "2026-12-31", 2026);

// Podlachien
        createSchoolHoliday("Frühjahrsferien", pl13, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl13, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl13, "2026-12-23", "2026-12-31", 2026);

// Pommern
        createSchoolHoliday("Frühjahrsferien", pl14, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl14, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl14, "2026-12-23", "2026-12-31", 2026);

// Schlesien
        createSchoolHoliday("Frühjahrsferien", pl15, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl15, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl15, "2026-12-23", "2026-12-31", 2026);

// Westpommern
        createSchoolHoliday("Frühjahrsferien", pl16, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", pl16, "2026-06-27", "2026-08-31", 2026);
        createSchoolHoliday("Weihnachtsferien", pl16, "2026-12-23", "2026-12-31", 2026);


        // Norwegian School Holidays 2026
// Agder
        createSchoolHoliday("Winterferien", no1, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no1, "2026-03-30", "2026-04-07", 2026);
        createSchoolHoliday("Osterferien", no1, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no1, "2026-06-20", "2026-08-12", 2026);
        createSchoolHoliday("Herbstferien", no1, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no1, "2026-11-06", "2026-11-06", 2026);
        createSchoolHoliday("Weihnachtsferien", no1, "2026-12-19", "2027-01-04", 2026);

// Akershus
        createSchoolHoliday("Winterferien", no2, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no2, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no2, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no2, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no2, "2026-11-11", "2026-11-11", 2026);
        createSchoolHoliday("Weihnachtsferien", no2, "2026-12-19", "2027-01-03", 2026);

// Buskerud
        createSchoolHoliday("Winterferien", no3, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", no3, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no3, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no3, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no3, "2026-11-11", "2026-11-11", 2026);
        createSchoolHoliday("Weihnachtsferien", no3, "2026-12-19", "2027-01-03", 2026);

// Finnmark
        createSchoolHoliday("Winterferien", no4, "2026-03-04", "2026-03-06", 2026);
        createSchoolHoliday("Osterferien", no4, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no4, "2026-06-20", "2026-06-20", 2026);

// Innlandet
        createSchoolHoliday("Winterferien", no5, "2026-02-16", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", no5, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no5, "2026-06-22", "2026-08-14", 2026);
        createSchoolHoliday("Herbstferien", no5, "2026-09-28", "2026-10-09", 2026);
        createSchoolHoliday("Herbstferien", no5, "2026-11-27", "2026-11-27", 2026);
        createSchoolHoliday("Weihnachtsferien", no5, "2026-12-21", "2027-01-01", 2026);

// Møre og Romsdal
        createSchoolHoliday("Winterferien", no6, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no6, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Osterferien", no6, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no6, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no6, "2026-10-05", "2026-10-09", 2026);
        createSchoolHoliday("Weihnachtsferien", no6, "2026-12-19", "2027-01-04", 2026);

// Nordland
        createSchoolHoliday("Winterferien", no7, "2026-03-02", "2026-03-06", 2026);
        createSchoolHoliday("Osterferien", no7, "2026-03-30", "2026-04-07", 2026);
        createSchoolHoliday("Osterferien", no7, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no7, "2026-06-20", "2026-08-12", 2026);
        createSchoolHoliday("Herbstferien", no7, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no7, "2026-11-20", "2026-11-20", 2026);
        createSchoolHoliday("Weihnachtsferien", no7, "2026-12-19", "2027-01-03", 2026);

// Oslo
        createSchoolHoliday("Winterferien", no8, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no8, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no8, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no8, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Weihnachtsferien", no8, "2026-12-21", "2027-01-01", 2026);

// Østfold
        createSchoolHoliday("Winterferien", no9, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no9, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Sommerferien", no9, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no9, "2026-09-28", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no9, "2026-11-19", "2026-11-19", 2026);
        createSchoolHoliday("Weihnachtsferien", no9, "2026-12-19", "2027-01-03", 2026);

// Rogaland
        createSchoolHoliday("Winterferien", no10, "2026-02-12", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", no10, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Herbstferien", no10, "2026-10-05", "2026-10-09", 2026);
        createSchoolHoliday("Herbstferien", no10, "2026-11-03", "2026-11-03", 2026);

// Telemark
        createSchoolHoliday("Winterferien", no11, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no11, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Osterferien", no11, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no11, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no11, "2026-10-05", "2026-10-09", 2026);
        createSchoolHoliday("Weihnachtsferien", no11, "2026-12-19", "2027-01-04", 2026);

// Trøndelag
        createSchoolHoliday("Winterferien", no12, "2026-02-20", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", no12, "2026-03-09", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no12, "2026-06-19", "2026-06-24", 2026);

// Troms
        createSchoolHoliday("Winterferien", no13, "2026-03-02", "2026-03-06", 2026);
        createSchoolHoliday("Osterferien", no13, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Osterferien", no13, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no13, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no13, "2026-09-30", "2026-10-02", 2026);
        createSchoolHoliday("Herbstferien", no13, "2026-11-16", "2026-11-17", 2026);
        createSchoolHoliday("Weihnachtsferien", no13, "2026-12-19", "2027-01-03", 2026);

// Vestfold
        createSchoolHoliday("Winterferien", no14, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", no14, "2026-03-30", "2026-04-06", 2026);
        createSchoolHoliday("Osterferien", no14, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no14, "2026-06-20", "2026-08-16", 2026);
        createSchoolHoliday("Herbstferien", no14, "2026-10-05", "2026-10-09", 2026);
        createSchoolHoliday("Weihnachtsferien", no14, "2026-12-19", "2027-01-04", 2026);

// Vestland
        createSchoolHoliday("Winterferien", no15, "2026-01-30", "2026-01-30", 2026);
        createSchoolHoliday("Osterferien", no15, "2026-03-28", "2026-04-06", 2026);
        createSchoolHoliday("Osterferien", no15, "2026-05-15", "2026-05-15", 2026);
        createSchoolHoliday("Sommerferien", no15, "2026-06-25", "2026-06-25", 2026);

        // Italian School Holidays 2026
// Abruzzo
        createSchoolHoliday("Winterferien", it1, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it1, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it1, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it1, "2026-12-23", "2027-01-06", 2026);

// Basilicata
        createSchoolHoliday("Winterferien", it2, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it2, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it2, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it2, "2026-12-23", "2027-01-06", 2026);

// Calabria
        createSchoolHoliday("Winterferien", it3, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it3, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it3, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it3, "2026-12-23", "2027-01-06", 2026);

// Campania
        createSchoolHoliday("Winterferien", it4, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it4, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it4, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it4, "2026-12-23", "2027-01-06", 2026);

// Emilia-Romagna
        createSchoolHoliday("Winterferien", it5, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it5, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it5, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it5, "2026-12-23", "2027-01-06", 2026);

// Friuli-Venezia Giulia
        createSchoolHoliday("Winterferien", it6, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it6, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it6, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it6, "2026-12-23", "2027-01-06", 2026);

// Lazio
        createSchoolHoliday("Winterferien", it7, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it7, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it7, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it7, "2026-12-23", "2027-01-06", 2026);

// Liguria
        createSchoolHoliday("Winterferien", it8, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it8, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it8, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it8, "2026-12-23", "2027-01-06", 2026);

// Lombardia
        createSchoolHoliday("Winterferien", it9, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it9, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it9, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it9, "2026-12-23", "2027-01-06", 2026);

// Marche
        createSchoolHoliday("Winterferien", it10, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it10, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it10, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it10, "2026-12-23", "2027-01-06", 2026);

// Molise
        createSchoolHoliday("Winterferien", it11, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it11, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it11, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it11, "2026-12-23", "2027-01-06", 2026);

// Piemonte
        createSchoolHoliday("Winterferien", it12, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it12, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it12, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it12, "2026-12-23", "2027-01-06", 2026);

// Puglia
        createSchoolHoliday("Winterferien", it13, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it13, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it13, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it13, "2026-12-23", "2027-01-06", 2026);

// Sardegna
        createSchoolHoliday("Winterferien", it14, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it14, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it14, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it14, "2026-12-23", "2027-01-06", 2026);

// Sicilia
        createSchoolHoliday("Winterferien", it15, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it15, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it15, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it15, "2026-12-23", "2027-01-06", 2026);

// Toscana
        createSchoolHoliday("Winterferien", it16, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it16, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it16, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it16, "2026-12-23", "2027-01-06", 2026);

// Trentino-Alto Adige
        createSchoolHoliday("Winterferien", it17, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it17, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it17, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it17, "2026-12-23", "2027-01-06", 2026);

// Umbria
        createSchoolHoliday("Winterferien", it18, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it18, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it18, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it18, "2026-12-23", "2027-01-06", 2026);

// Valle d'Aosta
        createSchoolHoliday("Winterferien", it19, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it19, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it19, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it19, "2026-12-23", "2027-01-06", 2026);

// Veneto
        createSchoolHoliday("Winterferien", it20, "2026-02-16", "2026-02-21", 2026);
        createSchoolHoliday("Osterferien", it20, "2026-04-02", "2026-04-07", 2026);
        createSchoolHoliday("Sommerferien", it20, "2026-06-11", "2026-09-14", 2026);
        createSchoolHoliday("Weihnachtsferien", it20, "2026-12-23", "2027-01-06", 2026);

        // Spanish School Holidays 2026
// Andalucía
        createSchoolHoliday("Winterferien", es1, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", es1, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es1, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es1, "2026-12-23", "2027-01-07", 2026);

// Aragón
        createSchoolHoliday("Winterferien", es2, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es2, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es2, "2026-06-22", "2026-09-08", 2026);
        createSchoolHoliday("Weihnachtsferien", es2, "2026-12-23", "2027-01-07", 2026);

// Asturias
        createSchoolHoliday("Winterferien", es3, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", es3, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es3, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es3, "2026-12-23", "2027-01-07", 2026);

// Islas Baleares
        createSchoolHoliday("Winterferien", es4, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es4, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es4, "2026-06-19", "2026-09-11", 2026);
        createSchoolHoliday("Weihnachtsferien", es4, "2026-12-23", "2027-01-07", 2026);

// Islas Canarias
        createSchoolHoliday("Winterferien", es5, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", es5, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es5, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es5, "2026-12-23", "2027-01-07", 2026);

// Cantabria
        createSchoolHoliday("Winterferien", es6, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", es6, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es6, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es6, "2026-12-23", "2027-01-07", 2026);

// Castilla y León
        createSchoolHoliday("Winterferien", es7, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es7, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es7, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es7, "2026-12-23", "2027-01-07", 2026);

// Castilla-La Mancha
        createSchoolHoliday("Winterferien", es8, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es8, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es8, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es8, "2026-12-23", "2027-01-07", 2026);

// Cataluña
        createSchoolHoliday("Winterferien", es9, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es9, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es9, "2026-06-22", "2026-09-07", 2026);
        createSchoolHoliday("Weihnachtsferien", es9, "2026-12-23", "2027-01-07", 2026);

// Extremadura
        createSchoolHoliday("Winterferien", es10, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es10, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es10, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es10, "2026-12-23", "2027-01-07", 2026);

// Galicia
        createSchoolHoliday("Winterferien", es11, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es11, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es11, "2026-06-22", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es11, "2026-12-23", "2027-01-07", 2026);

// Madrid
        createSchoolHoliday("Winterferien", es12, "2026-02-23", "2026-02-27", 2026);
        createSchoolHoliday("Osterferien", es12, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es12, "2026-06-24", "2026-09-08", 2026);
        createSchoolHoliday("Weihnachtsferien", es12, "2026-12-23", "2027-01-07", 2026);

// Murcia
        createSchoolHoliday("Winterferien", es13, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es13, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es13, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es13, "2026-12-23", "2027-01-07", 2026);

// Navarra
        createSchoolHoliday("Winterferien", es14, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es14, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es14, "2026-06-22", "2026-09-08", 2026);
        createSchoolHoliday("Weihnachtsferien", es14, "2026-12-23", "2027-01-07", 2026);

// País Vasco
        createSchoolHoliday("Winterferien", es15, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es15, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es15, "2026-06-22", "2026-09-07", 2026);
        createSchoolHoliday("Weihnachtsferien", es15, "2026-12-23", "2027-01-07", 2026);

// La Rioja
        createSchoolHoliday("Winterferien", es16, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es16, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es16, "2026-06-24", "2026-09-10", 2026);
        createSchoolHoliday("Weihnachtsferien", es16, "2026-12-23", "2027-01-07", 2026);

// Comunidad Valenciana
        createSchoolHoliday("Winterferien", es17, "2026-02-16", "2026-02-20", 2026);
        createSchoolHoliday("Osterferien", es17, "2026-04-02", "2026-04-13", 2026);
        createSchoolHoliday("Sommerferien", es17, "2026-06-22", "2026-09-07", 2026);
        createSchoolHoliday("Weihnachtsferien", es17, "2026-12-23", "2027-01-07", 2026);


        log.info("Data loading complete: {} countries, {} regions, {} school holidays", 
                countryRepository.count(), regionRepository.count(), schoolHolidayRepository.count());
    }

    private Country createCountry(String code, String name, Long population) {
        Country country = new Country();
        country.setCode(code);
        country.setName(name);
        country.setPopulation(population);
        return countryRepository.save(country);
    }

    private Region createRegion(String code, String name, Country country, Long population) {
        Region region = new Region();
        region.setCode(code);
        region.setName(name);
        region.setCountry(country);
        region.setPopulation(population);
        return regionRepository.save(region);
    }

    private void createSchoolHoliday(String name, Region region, String startDate, String endDate, int year) {
        SchoolHoliday sh = new SchoolHoliday();
        sh.setName(name);
        sh.setRegion(region);
        sh.setStartDate(LocalDate.parse(startDate));
        sh.setEndDate(LocalDate.parse(endDate));
        sh.setYear(year);
        schoolHolidayRepository.save(sh);
    }
}
