// Types based on Backend (Pratham's implementation)

export interface Country {
  id: number;
  code: string;
  name: string;
  population: number | null;
}

export interface Region {
  id: number;
  code: string;
  name: string;
  country: Country;
  population: number;
}

export interface Holiday {
  id: number;
  date: string;
  localName: string;
  englishName: string;
  countryCode: string;
  globalHoliday: boolean;
  types: string | null;
  region: Region | null;
  year: number;
  subdivisionCodes?: string[];
}

// Frontend-specific types for analysis
export interface SubdivisionInfo {
  code: string;
  name: string;
  countryCode: string;
  population: number;
}

export interface DayAnalysis {
  date: string;
  loadPercentage: number;
  level: 'very_low' | 'low' | 'medium_low' | 'medium' | 'high' | 'very_high';
  holidays: Holiday[];
}

export interface WeekendAnalysis {
  startDate: string;
  endDate: string;
  loadPercentage: number;
  affectedPopulation: number;
  level?: 'very_low' | 'low' | 'medium' | 'high' | 'very_high';
  affectedRegions?: string[];
  countryCode?: string;
}

export interface UpcomingHoliday {
  name: string;
  date: string;
  countryCode: string;
  isGlobal: boolean;
  affectedPopulation: number;
  regions: string[];
}

export interface SchoolHoliday {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  year: number;
  region: {
    id: number;
    code: string;
    name: string;
    population: number;
  };
}

export interface DayAnalysis {
  date: string;
  loadPercentage: number;
  level: 'very_low' | 'low' | 'medium_low' | 'medium' | 'high' | 'very_high';
  holidays: Holiday[];
  schoolHolidays?: SchoolHoliday[];
}