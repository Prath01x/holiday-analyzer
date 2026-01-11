import { Country, Holiday, SubdivisionInfo, DayAnalysis, WeekendAnalysis, UpcomingHoliday } from './types';

// Toggle für Mock-Daten
const USE_MOCK_DATA = false;

// Always use relative root. Dev server proxies /api to localhost:8080, and
// the deployed nginx serves /api via an internal upstream.
const API_BASE = '';

// Mock-Daten Generatoren
const generateMockCountries = (): Country[] => [
  { id: 1, code: 'DE', name: 'Deutschland', population: 83200000 },
  { id: 2, code: 'AT', name: 'Österreich', population: 8900000 },
  { id: 3, code: 'CH', name: 'Schweiz', population: 8700000 },
  { id: 4, code: 'FR', name: 'Frankreich', population: 67400000 },
  { id: 5, code: 'IT', name: 'Italien', population: 59000000 },
];

const generateMockSubdivisions = (): SubdivisionInfo[] => [
  { code: 'DE-BW', name: 'Baden-Württemberg', countryCode: 'DE', population: 11100000 },
  { code: 'DE-BY', name: 'Bayern', countryCode: 'DE', population: 13100000 },
  { code: 'DE-NW', name: 'Nordrhein-Westfalen', countryCode: 'DE', population: 17900000 },
  { code: 'DE-HE', name: 'Hessen', countryCode: 'DE', population: 6300000 },
  { code: 'AT-1', name: 'Burgenland', countryCode: 'AT', population: 300000 },
  { code: 'AT-2', name: 'Kärnten', countryCode: 'AT', population: 560000 },
  { code: 'AT-9', name: 'Wien', countryCode: 'AT', population: 1900000 },
];

const generateMockHolidays = (): Holiday[] => [
  {
    id: 1,
    date: '2025-12-25',
    localName: 'Weihnachten',
    englishName: 'Christmas Day',
    countryCode: 'DE',
    globalHoliday: true,
    types: 'Public',
    region: null,
    year: 2025
  },
  {
    id: 2,
    date: '2025-01-01',
    localName: 'Neujahr',
    englishName: 'New Year',
    countryCode: 'DE',
    globalHoliday: true,
    types: 'Public',
    region: null,
    year: 2025
  },
  {
    id: 3,
    date: '2025-01-06',
    localName: 'Heilige Drei Könige',
    englishName: 'Epiphany',
    countryCode: 'DE',
    globalHoliday: false,
    types: 'Public',
    region: { id: 1, code: 'DE-BW', name: 'Baden-Württemberg', country: { id: 1, code: 'DE', name: 'Deutschland', population: 83200000 }, population: 11100000 },
    year: 2025
  },
];

// Hilfsfunktion: Berechne Load Percentage
const calculateLoad = (affectedPopulation: number, totalPopulation: number): number => {
  return (affectedPopulation / totalPopulation) * 100;
};

// Hilfsfunktion: Bestimme Load Level
const getLoadLevel = (percentage: number): DayAnalysis['level'] => {
  if (percentage < 5) return 'very_low';      // 0-5%: Dunkelgrün 🟢
  if (percentage < 20) return 'low';          // 5-20%: Hellgrün 🟢
  if (percentage < 40) return 'medium_low';   // 20-40%: Gelbgrün 🟡
  if (percentage < 60) return 'medium';       // 40-60%: Orange 🟠
  if (percentage < 80) return 'high';         // 60-80%: Hellorange 🔴
  return 'very_high';                         // 80-100%: Knallrot 🔴
};
// Mock: Beste Wochenenden generieren
const generateMockWeekends = (
    count: number,
    countries: string[],
    regions: string[]
): WeekendAnalysis[] => {
  const mockCountries = generateMockCountries();
  const totalPopulation = mockCountries.reduce((sum, c) => sum + (c.population || 0), 0);

  const weekends: WeekendAnalysis[] = [];
  const startDate = new Date('2025-01-01');

  // Generiere Wochenenden für die nächsten 6 Monate
  for (let i = 0; i < 26; i++) {
    const weekendStart = new Date(startDate);
    weekendStart.setDate(startDate.getDate() + (i * 7) + 5); // Freitag

    const weekendEnd = new Date(weekendStart);
    weekendEnd.setDate(weekendStart.getDate() + 2); // Sonntag

    // Wenn Filter aktiv, nur für ausgewählte Länder
    const targetCountries = countries.length > 0 ? countries : mockCountries.map(c => c.code);

    targetCountries.forEach(countryCode => {
      const country = mockCountries.find(c => c.code === countryCode);
      if (!country) return;

      // Zufällige Auslastung
      const affectedPop = Math.floor(Math.random() * (country.population || 0));
      const loadPercentage = calculateLoad(affectedPop, totalPopulation);

      weekends.push({
        startDate: weekendStart.toISOString().split('T')[0],
        endDate: weekendEnd.toISOString().split('T')[0],
        loadPercentage: Math.round(loadPercentage * 10) / 10,
        affectedPopulation: affectedPop,
        affectedRegions: regions.length > 0 ? regions : undefined,
        countryCode: countries.length > 0 ? countryCode : undefined
      });
    });
  }

  // Sortiere nach Load (niedrigste zuerst) und gib nur count zurück
  return weekends
      .sort((a, b) => a.loadPercentage - b.loadPercentage)
      .slice(0, count);
};

// Mock: Kommende Feiertage generieren
const generateMockUpcomingHolidays = (
    days: number,
    countries: string[],
    regions: string[]
): UpcomingHoliday[] => {
  const today = new Date();
  const endDate = new Date(today);
  endDate.setDate(today.getDate() + days);

  const holidays: UpcomingHoliday[] = [
    {
      name: 'Weihnachten',
      date: '2025-12-25',
      countryCode: 'DE',
      isGlobal: true,
      affectedPopulation: 83200000,
      regions: []
    },
    {
      name: '2. Weihnachtsfeiertag',
      date: '2025-12-26',
      countryCode: 'DE',
      isGlobal: true,
      affectedPopulation: 83200000,
      regions: []
    },
    {
      name: 'Weihnachten',
      date: '2025-12-25',
      countryCode: 'AT',
      isGlobal: true,
      affectedPopulation: 8900000,
      regions: []
    },
    {
      name: 'Neujahr',
      date: '2026-01-01',
      countryCode: 'DE',
      isGlobal: true,
      affectedPopulation: 83200000,
      regions: []
    },
    {
      name: 'Heilige Drei Könige',
      date: '2026-01-06',
      countryCode: 'DE',
      isGlobal: false,
      affectedPopulation: 24200000,
      regions: ['DE-BW', 'DE-BY']
    },
  ];

  // Filtere nach Auswahl
  const filtered = holidays.filter(h => {
    const holidayDate = new Date(h.date);
    if (holidayDate < today || holidayDate > endDate) return false;

    // Länder-Filter
    if (countries.length > 0 && !countries.includes(h.countryCode)) return false;

    // Regionen-Filter
    if (regions.length > 0) {
      if (h.regions && h.regions.length > 0) {
        return h.regions.some(r => regions.includes(r));
      }
      return false;
    }

    return true;
  });

  // Sortiere nach Datum und dann nach Population
  return filtered.sort((a, b) => {
    const dateCompare = new Date(a.date).getTime() - new Date(b.date).getTime();
    if (dateCompare !== 0) return dateCompare;
    return b.affectedPopulation - a.affectedPopulation;
  });
};

// API Interface
export const api = {
  // Countries
  async getCountries(): Promise<Country[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockCountries());
    }
    const response = await fetch(`${API_BASE}/api/countries`);
    return response.json();
  },

  // Subdivisions
  async getSubdivisions(): Promise<SubdivisionInfo[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockSubdivisions());
    }
    const response = await fetch(`${API_BASE}/api/regions`);
    return response.json();
  },

  // Holidays
  async getHolidays(country?: string, year?: number, subdivision?: string): Promise<Holiday[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockHolidays());
    }
    const params = new URLSearchParams();
    if (country) params.append('country', country);
    if (year) params.append('year', year.toString());
    if (subdivision) params.append('subdivision', subdivision);

    const response = await fetch(`${API_BASE}/api/holidays?${params}`);
    return response.json();
  },

  async analyzeDateRange(
      startDate: string,
      endDate: string,
      country?: string,
      subdivision?: string
  ): Promise<DayAnalysis[]> {
    if (USE_MOCK_DATA) {
      // Generiere Mock-Daten für jeden Tag im Zeitraum
      const analyses: DayAnalysis[] = [];
      const start = new Date(startDate);
      const end = new Date(endDate);

      const mockHolidays = generateMockHolidays();

      for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
        const dateStr = d.toISOString().split('T')[0];

        // Finde Feiertage für diesen Tag
        const dayHolidays = mockHolidays.filter(h => h.date === dateStr);

        // Berechne Auslastung basierend auf Feiertagen
        let loadPercentage = 0;

        if (dayHolidays.length > 0) {
          // Wenn es Feiertage gibt, berechne Load
          const globalHolidays = dayHolidays.filter(h => h.globalHoliday);
          const regionalHolidays = dayHolidays.filter(h => !h.globalHoliday);

          if (globalHolidays.length > 0) {
            // Globaler Feiertag = 80-95% Load
            loadPercentage = 80 + Math.random() * 15;
          } else if (regionalHolidays.length > 0) {
            // Regionaler Feiertag = 20-40% Load
            loadPercentage = 20 + Math.random() * 20;
          }
        } else {
          // Kein Feiertag = 0-15% Load
          loadPercentage = Math.random() * 15;
        }

        // Bestimme Level
        let level: DayAnalysis['level'];
        if (loadPercentage < 10) level = 'very_low';
        else if (loadPercentage < 25) level = 'low';
        else if (loadPercentage < 50) level = 'medium';
        else if (loadPercentage < 75) level = 'high';
        else level = 'very_high';

        analyses.push({
          date: dateStr,
          loadPercentage: Math.round(loadPercentage),
          level: level,
          holidays: dayHolidays
        });
      }

      return Promise.resolve(analyses);
    }

    // Determine which years we need to fetch
    const startYear = new Date(startDate).getFullYear();
    const endYear = new Date(endDate).getFullYear();
    const countryCode = country || 'DE';

    // Create a map to store all day analyses
    const allDaysMap = new Map<string, DayAnalysis>();

    // Fetch data for each year separately
    for (let year = startYear; year <= endYear; year++) {
      try {
        // Fetch vacation load data for this year (enthält bereits Schulferien + Feiertage berechnet)
        const vacationResponse = await fetch(
            `${API_BASE}/api/vacation-load?countryCode=${countryCode}&year=${year}`
        );
        const vacationData = await vacationResponse.json();

        // Get country population from vacation data
        const countryPopulation = vacationData.countryPopulation || 83240000;

        // Transform dailyLoads to DayAnalysis
        for (const dailyLoad of vacationData.dailyLoads) {
          const dateStr = dailyLoad.date;
          const date = new Date(dateStr);

          // Only include dates in our range
          if (date >= new Date(startDate) && date <= new Date(endDate)) {
            const totalPop = dailyLoad.totalPopulation || 0;
            const loadPercentage = countryPopulation > 0
                ? Math.round((totalPop / countryPopulation) * 100)
                : 0;

            allDaysMap.set(dateStr, {
              date: dateStr,
              loadPercentage,
              level: getLoadLevel(loadPercentage),
              holidays: [],
              schoolHolidays: []
            });
          }
        }
      } catch (error) {
        console.error(`Error fetching vacation load for year ${year}:`, error);
      }
    }

    // Now fetch holidays AND school holidays from new endpoint
    try {
      const params = new URLSearchParams({
        country: countryCode,
        startDate,
        endDate
      });

      if (subdivision) {
        params.append('subdivision', subdivision);
      }

      const analysisResponse = await fetch(`${API_BASE}/api/vacation-analysis?${params}`);
      const analysisData = await analysisResponse.json();

      // Add holidays to the corresponding days
      for (const holiday of analysisData.holidays) {
        const day = allDaysMap.get(holiday.date);
        if (day) {
          day.holidays.push(holiday);
        }
      }

      // Add school holidays to the corresponding days
      for (const schoolHoliday of analysisData.schoolHolidays) {
        const start = new Date(schoolHoliday.startDate);
        const end = new Date(schoolHoliday.endDate);

        // Add this school holiday to every day in its range
        for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
          const dateStr = d.toISOString().split('T')[0];
          const day = allDaysMap.get(dateStr);
          if (day) {
            if (!day.schoolHolidays) {
              day.schoolHolidays = [];
            }
            // Avoid duplicates
            if (!day.schoolHolidays.find(sh => sh.id === schoolHoliday.id)) {
              day.schoolHolidays.push(schoolHoliday);
            }
          }
        }
      }
    } catch (error) {
      console.error('Error fetching vacation analysis:', error);
    }

    // Convert map to array and sort by date
    const analyses = Array.from(allDaysMap.values()).sort((a, b) =>
        a.date.localeCompare(b.date)
    );

    return analyses;
  },

  // Best Weekends (alle Länder)
  async getBestWeekends(count: number = 10): Promise<WeekendAnalysis[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockWeekends(count, [], []));
    }
    const response = await fetch(`${API_BASE}/api/analysis/best-weekends?count=${count}`);
    return response.json();
  },

  // Best Weekends (gefiltert)
  async getBestWeekendsFiltered(
      count: number,
      countries: string[],
      regions: string[]
  ): Promise<WeekendAnalysis[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockWeekends(count, countries, regions));
    }
    const params = new URLSearchParams({ count: count.toString() });
    countries.forEach(c => params.append('countries', c));
    regions.forEach(r => params.append('regions', r));

    const response = await fetch(`${API_BASE}/api/analysis/best-weekends?${params}`);
    return response.json();
  },

  // Upcoming Holidays (alle Länder)
  async getUpcomingHolidays(days: number = 28): Promise<UpcomingHoliday[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockUpcomingHolidays(days, [], []));
    }
    const response = await fetch(`${API_BASE}/api/analysis/upcoming?days=${days}`);
    return response.json();
  },

  // Upcoming Holidays (gefiltert)
  async getUpcomingHolidaysFiltered(
      days: number,
      countries: string[],
      regions: string[]
  ): Promise<UpcomingHoliday[]> {
    if (USE_MOCK_DATA) {
      return Promise.resolve(generateMockUpcomingHolidays(days, countries, regions));
    }
    const params = new URLSearchParams({ days: days.toString() });
    countries.forEach(c => params.append('countries', c));
    regions.forEach(r => params.append('regions', r));

    const response = await fetch(`${API_BASE}/api/analysis/upcoming?${params}`);
    return response.json();
  },
};