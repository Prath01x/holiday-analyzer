import { useState, useEffect } from 'react';
import VacationPlanner from './VacationPlanner';
import BestWeekends from './unused/BestWeekends.tsx';
import CurrentHolidays from './unused/CurrentHolidays.tsx';
import { api } from '../api';
import { Country, SubdivisionInfo, UpcomingHoliday, WeekendAnalysis } from '../types';
import './Dashboard.css';

const Dashboard = () => {
  const [countries, setCountries] = useState<Country[]>([]);
  const [subdivisions, setSubdivisions] = useState<SubdivisionInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [selectedRegions, setSelectedRegions] = useState<string[]>([]);
  const [filteredWeekends, setFilteredWeekends] = useState<WeekendAnalysis[]>([]);
  const [filteredHolidays, setFilteredHolidays] = useState<UpcomingHoliday[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [countriesData, subdivisionsData] = await Promise.all([
          api.getCountries(),
          api.getSubdivisions()
        ]);
        setCountries(countriesData);
        setSubdivisions(subdivisionsData);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchFilteredData = async () => {
      if (selectedCountries.length === 0) {
        const [weekends, holidays] = await Promise.all([
          api.getBestWeekends(10),
          api.getUpcomingHolidays(28)
        ]);
        setFilteredWeekends(weekends);
        setFilteredHolidays(holidays);
      } else {
        const [weekends, holidays] = await Promise.all([
          api.getBestWeekendsFiltered(10, selectedCountries, selectedRegions),
          api.getUpcomingHolidaysFiltered(28, selectedCountries, selectedRegions)
        ]);
        setFilteredWeekends(weekends);
        setFilteredHolidays(holidays);
      }
    };
    fetchFilteredData();
  }, [selectedCountries, selectedRegions]);

  if (loading) {
    return <div className="loading">Lade Daten...</div>;
  }

  return (
      <div className="dashboard">
        <header className="dashboard-header">
          <h1>Holiday Analyzer</h1>
          <p className="dashboard-subtitle">
            Analysiere Feiertage und Ferienzeiten in Europa
          </p>
        </header>

        <div className="dashboard-content">
          <div className="dashboard-center" style={{ gridColumn: '1 / -1' }}>
            <VacationPlanner
                countries={countries}
                subdivisions={subdivisions}
                onFilterChange={(countries, regions) => {
                  setSelectedCountries(countries);
                  setSelectedRegions(regions);
                }}
            />
          </div>
        </div>
      </div>
  );
};

export default Dashboard;