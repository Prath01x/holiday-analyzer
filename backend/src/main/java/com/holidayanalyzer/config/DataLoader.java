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
        createRegion("AT-1", "Burgenland", at, 294000L);
        createRegion("AT-2", "Kärnten", at, 561000L);
        createRegion("AT-3", "Niederösterreich", at, 1690000L);
        createRegion("AT-4", "Oberösterreich", at, 1490000L);
        createRegion("AT-5", "Salzburg", at, 560000L);
        createRegion("AT-6", "Steiermark", at, 1250000L);
        createRegion("AT-7", "Tirol", at, 760000L);
        createRegion("AT-8", "Vorarlberg", at, 400000L);
        createRegion("AT-9", "Wien", at, 1900000L);

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
        createRegion("ES-AN", "Andalucía", es, 8470000L);
        createRegion("ES-AR", "Aragón", es, 1320000L);
        createRegion("ES-AS", "Asturias", es, 1020000L);
        createRegion("ES-IB", "Islas Baleares", es, 1170000L);
        createRegion("ES-CN", "Islas Canarias", es, 2170000L);
        createRegion("ES-CB", "Cantabria", es, 580000L);
        createRegion("ES-CL", "Castilla y León", es, 2400000L);
        createRegion("ES-CM", "Castilla-La Mancha", es, 2040000L);
        createRegion("ES-CT", "Cataluña", es, 7670000L);
        createRegion("ES-EX", "Extremadura", es, 1070000L);
        createRegion("ES-GA", "Galicia", es, 2700000L);
        createRegion("ES-MD", "Madrid", es, 6750000L);
        createRegion("ES-MC", "Murcia", es, 1510000L);
        createRegion("ES-NC", "Navarra", es, 660000L);
        createRegion("ES-PV", "País Vasco", es, 2210000L);
        createRegion("ES-RI", "La Rioja", es, 320000L);
        createRegion("ES-VC", "Comunidad Valenciana", es, 5060000L);

        // Load Dutch regions
        createRegion("NL-DR", "Drenthe", nl, 493000L);
        createRegion("NL-FL", "Flevoland", nl, 430000L);
        createRegion("NL-FR", "Friesland", nl, 650000L);
        createRegion("NL-GE", "Gelderland", nl, 2090000L);
        createRegion("NL-GR", "Groningen", nl, 585000L);
        createRegion("NL-LI", "Limburg", nl, 1120000L);
        createRegion("NL-NB", "Noord-Brabant", nl, 2570000L);
        createRegion("NL-NH", "Noord-Holland", nl, 2880000L);
        createRegion("NL-OV", "Overijssel", nl, 1160000L);
        createRegion("NL-UT", "Utrecht", nl, 1360000L);
        createRegion("NL-ZE", "Zeeland", nl, 385000L);
        createRegion("NL-ZH", "Zuid-Holland", nl, 3710000L);

        // Load Italian regions
        createRegion("IT-65", "Abruzzo", it, 1300000L);
        createRegion("IT-77", "Basilicata", it, 560000L);
        createRegion("IT-78", "Calabria", it, 1920000L);
        createRegion("IT-72", "Campania", it, 5790000L);
        createRegion("IT-45", "Emilia-Romagna", it, 4460000L);
        createRegion("IT-36", "Friuli-Venezia Giulia", it, 1210000L);
        createRegion("IT-62", "Lazio", it, 5880000L);
        createRegion("IT-42", "Liguria", it, 1540000L);
        createRegion("IT-25", "Lombardia", it, 10060000L);
        createRegion("IT-57", "Marche", it, 1520000L);
        createRegion("IT-67", "Molise", it, 300000L);
        createRegion("IT-21", "Piemonte", it, 4340000L);
        createRegion("IT-75", "Puglia", it, 4020000L);
        createRegion("IT-88", "Sardegna", it, 1630000L);
        createRegion("IT-82", "Sicilia", it, 4970000L);
        createRegion("IT-52", "Toscana", it, 3730000L);
        createRegion("IT-32", "Trentino-Alto Adige", it, 1080000L);
        createRegion("IT-55", "Umbria", it, 880000L);
        createRegion("IT-23", "Valle d'Aosta", it, 125000L);
        createRegion("IT-34", "Veneto", it, 4910000L);

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
