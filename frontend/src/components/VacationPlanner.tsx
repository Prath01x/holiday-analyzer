import { useState, useEffect } from 'react';
import Calendar from './Calendar';
import SelectedRangeInfo from './SelectedRangeInfo';
import { Country, SubdivisionInfo, DayAnalysis } from '../types';
import { api } from '../api';
import './VacationPlanner.css';

interface Props {
  countries: Country[];
  subdivisions: SubdivisionInfo[];
  onFilterChange: (countries: string[], regions: string[]) => void;
}

const VacationPlanner = ({ countries, subdivisions, onFilterChange }: Props) => {
  const [selectedCountry, setSelectedCountry] = useState<string>('');
  const [selectedRegion, setSelectedRegion] = useState<string>('');
  const [availableRegions, setAvailableRegions] = useState<SubdivisionInfo[]>([]);

  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  const [calendarData, setCalendarData] = useState<DayAnalysis[]>([]);
  const [loading, setLoading] = useState(false);

  // Aktualisiere verfügbare Regionen wenn Land gewählt wird
  useEffect(() => {
    if (selectedCountry) {
      const regions = subdivisions.filter(s => s.countryCode === selectedCountry);
      setAvailableRegions(regions);
      setSelectedRegion('');
    } else {
      setAvailableRegions([]);
      setSelectedRegion('');
    }
  }, [selectedCountry, subdivisions]);

  // Informiere Parent über Filter-Änderungen
  useEffect(() => {
    const selectedCountries = selectedCountry ? [selectedCountry] : [];
    const selectedRegions = selectedRegion ? [selectedRegion] : [];
    onFilterChange(selectedCountries, selectedRegions);
  }, [selectedCountry, selectedRegion, onFilterChange]);

  // Lade Kalender-Daten für das gesamte Jahr
  useEffect(() => {
    const loadCalendarData = async () => {
      setLoading(true);
      try {
        // Load data for current year and next year to cover all possible date selections
        const currentYear = new Date().getFullYear();
        const startDate = `${currentYear}-01-01`;
        const endDate = `${currentYear + 2}-12-31`;

        console.log('VacationPlanner loading data from', startDate, 'to', endDate);

        const data = await api.analyzeDateRange(
            startDate,
            endDate,
            selectedCountry || undefined,
            selectedRegion || undefined
        );
        setCalendarData(data);
        console.log('VacationPlanner loaded', data.length, 'days of data');
      } catch (error) {
        console.error('Error loading calendar data:', error);
      } finally {
        setLoading(false);
      }
    };

    loadCalendarData();
  }, [selectedCountry, selectedRegion]);

  const handleCountryChange = (countryCode: string) => {
    setSelectedCountry(countryCode);
  };

  const handleRegionChange = (regionCode: string) => {
    setSelectedRegion(regionCode);
  };

  const handleReset = () => {
    setSelectedCountry('');
    setSelectedRegion('');
    setStartDate('');
    setEndDate('');
  };

  const handleDateSelect = (date: string) => {
    if (!date) {
      // Reset
      setStartDate('');
      setEndDate('');
      return;
    }

    if (!startDate || (startDate && endDate)) {
      // Neuen Zeitraum starten
      setStartDate(date);
      setEndDate('');
    } else if (startDate && !endDate) {
      // End-Datum setzen
      if (date >= startDate) {
        setEndDate(date);
      } else {
        // Wenn früheres Datum gewählt: Start-Datum neu setzen
        setEndDate(startDate);
        setStartDate(date);
      }
    }
  };

  return (
      <div className="vacation-planner">
        <h2>Urlaubsplaner</h2>

        <div className="planner-controls">
          <div className="control-group">
            <label>Land auswählen</label>
            <select
                value={selectedCountry}
                onChange={(e) => handleCountryChange(e.target.value)}
            >
              <option value="">Alle Länder</option>
              {countries.map(country => (
                  <option key={country.id} value={country.code}>
                    {country.name}
                  </option>
              ))}
            </select>
          </div>

          {selectedCountry && availableRegions.length > 0 && (
              <div className="control-group">
                <label>Region auswählen (optional)</label>
                <select
                    value={selectedRegion}
                    onChange={(e) => handleRegionChange(e.target.value)}
                >
                  <option value="">Alle Regionen in {countries.find(c => c.code === selectedCountry)?.name}</option>
                  {availableRegions.map(region => (
                      <option key={region.code} value={region.code}>
                        {region.name}
                      </option>
                  ))}
                </select>
              </div>
          )}

          <div className="date-selection-info">
            <p className="instruction">
              {!startDate && !endDate && '📅 Klicke auf ein Datum um den Zeitraum zu starten'}
              {startDate && !endDate && '📅 Klicke auf ein weiteres Datum um den Zeitraum zu beenden'}
              {startDate && endDate && (
                  <>
                    <strong>Ausgewählter Zeitraum:</strong> {new Date(startDate).toLocaleDateString('de-DE')} - {new Date(endDate).toLocaleDateString('de-DE')}
                  </>
              )}
            </p>
            {(startDate || endDate) && (
                <button className="btn-reset-small" onClick={handleReset}>
                  Zeitraum zurücksetzen
                </button>
            )}
          </div>
        </div>

        {selectedCountry && (
            <div className="filter-info">
          <span className="filter-badge">
            Filter aktiv: {countries.find(c => c.code === selectedCountry)?.name}
            {selectedRegion && ` → ${availableRegions.find(r => r.code === selectedRegion)?.name}`}
          </span>
            </div>
        )}

        <div className="calendar-and-info">
          <Calendar
              dayAnalyses={calendarData}
              startDate={startDate}
              endDate={endDate}
              onDateSelect={handleDateSelect}
              loading={loading}
          />

          {startDate && endDate && (
              <SelectedRangeInfo
                  startDate={startDate}
                  endDate={endDate}
                  dayAnalyses={calendarData}
              />
          )}
        </div>
      </div>
  );
};

export default VacationPlanner;