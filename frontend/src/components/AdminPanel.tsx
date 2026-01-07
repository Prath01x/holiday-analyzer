import { useState, useEffect } from 'react';
import { api } from '../api';
import { Country, Holiday, SubdivisionInfo } from '../types';
import AdminHeader from './AdminHeader';
import './AdminPanel.css';

interface Props {
  onBack: () => void;
}

type AdminTab = 'overview' | 'countries' | 'holidays';
type HolidayOrVacation = 'holiday' | 'vacation';
type Scope = 'nationwide' | 'regional';

interface MockRegion {
  code: string;
  name_de: string;
  name_en: string;
  countryCode: string;
}

interface SchoolHoliday {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  year: number;
  region: {
    id: number;
    code: string;
    name: string;
  };
}

const AdminPanel = ({ onBack }: Props) => {
  const [activeTab, setActiveTab] = useState<AdminTab>('overview');
  const [countries, setCountries] = useState<Country[]>([]);
  const [subdivisions, setSubdivisions] = useState<SubdivisionInfo[]>([]);
  const [holidays, setHolidays] = useState<Holiday[]>([]);
  const [schoolHolidays, setSchoolHolidays] = useState<SchoolHoliday[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const [countryForm, setCountryForm] = useState({
    code: '',
    name_de: '',
    name_en: '',
    population: 0,
  });
  const [regionForm, setRegionForm] = useState({
    countryCode: '',
    code: '',
    name_de: '',
    name_en: '',
    population: 0,
  });
  const [editingCountry, setEditingCountry] = useState<Country | null>(null);

  // Holiday Form State
  const [holidayOrVacation, setHolidayOrVacation] = useState<HolidayOrVacation>('holiday');
  const [scope, setScope] = useState<Scope>('nationwide');
  const [holidayForm, setHolidayForm] = useState({
    name_de: '',
    name_en: '',
    date: '', // Nur für Feiertag
    startDate: '', // Für Ferien
    endDate: '', // Für Ferien
    countryCode: '',
    regionCodes: [] as string[],
  });

  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState<Holiday[]>([]);
  const [editingHoliday, setEditingHoliday] = useState<Holiday | null>(null);

  // CSV Import variables - commented out until backend endpoint is ready
  // const [csvFile, setCsvFile] = useState<File | null>(null);
  // const [csvType] = useState<'country' | 'region' | 'holiday'>('country');

  // Mock Regionen - später vom Backend laden
  const mockRegionsByCountry: Record<string, MockRegion[]> = {
    'DE': [
      { code: 'DE-BW', name_de: 'Baden-Württemberg', name_en: 'Baden-Württemberg', countryCode: 'DE' },
      { code: 'DE-BY', name_de: 'Bayern', name_en: 'Bavaria', countryCode: 'DE' },
      { code: 'DE-NW', name_de: 'Nordrhein-Westfalen', name_en: 'North Rhine-Westphalia', countryCode: 'DE' },
      { code: 'DE-HE', name_de: 'Hessen', name_en: 'Hesse', countryCode: 'DE' },
      { code: 'DE-SN', name_de: 'Sachsen', name_en: 'Saxony', countryCode: 'DE' },
    ],
    'AT': [
      { code: 'AT-1', name_de: 'Burgenland', name_en: 'Burgenland', countryCode: 'AT' },
      { code: 'AT-2', name_de: 'Kärnten', name_en: 'Carinthia', countryCode: 'AT' },
      { code: 'AT-9', name_de: 'Wien', name_en: 'Vienna', countryCode: 'AT' },
    ],
    'FR': [
      { code: 'FR-IDF', name_de: 'Île-de-France', name_en: 'Île-de-France', countryCode: 'FR' },
      { code: 'FR-PAC', name_de: 'Provence-Alpes-Côte d\'Azur', name_en: 'Provence-Alpes-Côte d\'Azur', countryCode: 'FR' },
    ],
  };

  useEffect(() => {
    fetchCountries();
    fetchSubdivisions();
    fetchHolidays();
    fetchSchoolHolidays();
  }, []);

  const fetchCountries = async () => {
    try {
      const data = await api.getCountries();
      setCountries(data);
    } catch (error) {
      console.error('Error fetching countries:', error);
    }
  };

  const fetchSubdivisions = async () => {
    try {
      const data = await api.getSubdivisions();
      setSubdivisions(data);
    } catch (error) {
      console.error('Error fetching subdivisions:', error);
    }
  };

  const fetchHolidays = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/holidays');
      const data = await response.json();
      setHolidays(data);
    } catch (error) {
      console.error('Error fetching holidays:', error);
    }
  };

  const fetchSchoolHolidays = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/school-holidays');
      const data = await response.json();
      setSchoolHolidays(data);
    } catch (error) {
      console.error('Error fetching school holidays:', error);
    }
  };

  const showMessage = (type: 'success' | 'error', text: string) => {
    setMessage({ type, text });
    setTimeout(() => setMessage(null), 5000);
  };

  // Country/Region Management

  const handleCountrySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // TODO: Backend Endpoint POST /api/admin/countries
      showMessage('success', `Land ${countryForm.name_de} erfolgreich ${editingCountry ? 'aktualisiert' : 'erstellt'}`);
      setCountryForm({ code: '', name_de: '', name_en: '', population: 0 });
      setEditingCountry(null);
      fetchCountries();
    } catch (error) {
      showMessage('error', 'Fehler beim Speichern des Landes');
    } finally {
      setLoading(false);
    }
  };

  const handleRegionSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // TODO: Backend Endpoint POST /api/admin/subdivisions
      showMessage('success', `Region ${regionForm.name_de} erfolgreich erstellt`);
      setRegionForm({ countryCode: '', code: '', name_de: '', name_en: '', population: 0 });
    } catch (error) {
      showMessage('error', 'Fehler beim Erstellen der Region');
    } finally {
      setLoading(false);
    }
  };

  const handleEditCountry = (country: Country) => {
    setEditingCountry(country);
    setCountryForm({
      code: country.code,
      name_de: country.name,
      name_en: country.name,
      population: country.population || 0,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDeleteCountry = async (id: number) => {
    if (!window.confirm('Land wirklich löschen? Alle zugehörigen Regionen und Feiertage gehen verloren (CASCADE).')) {
      return;
    }

    try {
      // TODO: Backend Endpoint DELETE /api/admin/countries/{id}
      console.log('Delete country:', id);
      showMessage('success', 'Land gelöscht');
      fetchCountries();
    } catch (error) {
      showMessage('error', 'Fehler beim Löschen');
    }
  };

  // Holiday Management

  const handleHolidaySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const holidaysToCreate = [];

      // Bestimme Start- und Enddatum
      let startDate: string;
      let endDate: string;

      if (holidayOrVacation === 'holiday') {
        // Feiertag: start = end
        startDate = holidayForm.date;
        endDate = holidayForm.date;
      } else {
        // Ferien: start != end
        startDate = holidayForm.startDate;
        endDate = holidayForm.endDate;
      }

      if (scope === 'nationwide') {
        // Landesweit: Ein Eintrag, subdivision_code = NULL
        holidaysToCreate.push({
          start_date: startDate,
          end_date: endDate,
          name_de: holidayForm.name_de,
          name_en: holidayForm.name_en,
          country_code: holidayForm.countryCode,
          subdivision_code: null,
          year: new Date(startDate).getFullYear(),
        });
      } else {
        // Regional: Ein Eintrag pro ausgewählter Region
        for (const regionCode of holidayForm.regionCodes) {
          holidaysToCreate.push({
            start_date: startDate,
            end_date: endDate,
            name_de: holidayForm.name_de,
            name_en: holidayForm.name_en,
            country_code: holidayForm.countryCode,
            subdivision_code: regionCode,
            year: new Date(startDate).getFullYear(),
          });
        }
      }

      showMessage('success', `${holidaysToCreate.length} Eintrag/Einträge erfolgreich gespeichert`);
      resetHolidayForm();
    } catch (error) {
      showMessage('error', 'Fehler beim Speichern');
    } finally {
      setLoading(false);
    }
  };

  const handleSearchHolidays = async () => {
    if (!searchTerm.trim()) {
      showMessage('error', 'Bitte Suchbegriff eingeben');
      return;
    }

    setLoading(true);
    try {
      // TODO: Backend Endpoint GET /api/admin/holidays/search?term={searchTerm}
      setSearchResults([]);
      showMessage('success', 'Suche durchgeführt');
    } catch (error) {
      showMessage('error', 'Fehler bei der Suche');
    } finally {
      setLoading(false);
    }
  };

  const handleEditHoliday = (holiday: Holiday) => {
    setEditingHoliday(holiday);
    setHolidayForm({
      name_de: holiday.localName,
      name_en: holiday.englishName || holiday.localName,
      date: holiday.date,
      startDate: holiday.date,
      endDate: holiday.date,
      countryCode: holiday.countryCode,
      regionCodes: holiday.region?.code ? [holiday.region.code] : [],
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDeleteHoliday = async (holidayId: number) => {
    if (!window.confirm('Feiertag wirklich löschen?')) {
      return;
    }

    try {
      // TODO: Backend Endpoint DELETE /api/admin/holidays/{id}
      showMessage('success', 'Gelöscht');
      setSearchResults(searchResults.filter(h => h.id !== holidayId));
    } catch (error) {
      showMessage('error', 'Fehler beim Löschen');
    }
  };

  const resetHolidayForm = () => {
    setHolidayForm({
      name_de: '',
      name_en: '',
      date: '',
      startDate: '',
      endDate: '',
      countryCode: '',
      regionCodes: [],
    });
    setHolidayOrVacation('holiday');
    setScope('nationwide');
    setEditingHoliday(null);
  };

  // CSV Import - TODO: Implement when backend endpoint is ready
  // Commented out to avoid ESLint unused variable warning
  // const handleCSVImport = async (e: React.FormEvent) => {
  //   e.preventDefault();
  //   if (!csvFile) {
  //     showMessage('error', 'Bitte CSV-Datei auswählen');
  //     return;
  //   }
  //   setLoading(true);
  //   try {
  //     console.log('Import CSV:', csvFile.name);
  //     showMessage('success', `CSV erfolgreich importiert: ${csvFile.name}`);
  //     setCsvFile(null);
  //     if (csvType === 'country') {
  //       fetchCountries();
  //     }
  //   } catch (error) {
  //     showMessage('error', 'Fehler beim CSV-Import');
  //   } finally {
  //     setLoading(false);
  //   }
  // };

  // Selection Helpers

  const toggleRegionSelection = (code: string) => {
    setHolidayForm(prev => ({
      ...prev,
      regionCodes: prev.regionCodes.includes(code)
          ? prev.regionCodes.filter(c => c !== code)
          : [...prev.regionCodes, code],
    }));
  };

  const getAvailableRegions = (): MockRegion[] => {
    if (!holidayForm.countryCode) return [];
    return mockRegionsByCountry[holidayForm.countryCode] || [];
  };

  return (
      <div className="admin-panel">
        <AdminHeader onBack={onBack} />

        {message && (
            <div className={`message message-${message.type}`}>
              {message.text}
            </div>
        )}

        <div className="admin-tabs">
          <button
              className={`tab ${activeTab === 'overview' ? 'active' : ''}`}
              onClick={() => setActiveTab('overview')}
          >
            Übersicht
          </button>
          <button
              className={`tab ${activeTab === 'countries' ? 'active' : ''}`}
              onClick={() => setActiveTab('countries')}
          >
            Länder & Regionen
          </button>
          <button
              className={`tab ${activeTab === 'holidays' ? 'active' : ''}`}
              onClick={() => setActiveTab('holidays')}
          >
            Feiertage & Ferien
          </button>
        </div>

        <div className="admin-content">
          {/* OVERVIEW TAB */}
          {activeTab === 'overview' && (
              <div className="admin-section">
                <h2>Datenübersicht</h2>

                <div className="stats-grid">
                  <div className="stat-card">
                    <div className="stat-number">{countries.length}</div>
                    <div className="stat-label">Länder</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-number">{subdivisions.length}</div>
                    <div className="stat-label">Regionen</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-number">{holidays.length}</div>
                    <div className="stat-label">Feiertage</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-number">{schoolHolidays.length}</div>
                    <div className="stat-label">Ferienperioden</div>
                  </div>
                </div>

                <h3>Verfügbare Länder</h3>
                <div className="countries-list">
                  {countries.map(country => (
                      <div key={country.id} className="country-item">
                        <div className="country-info">
                          <span className="country-code">{country.code}</span>
                          <span className="country-name">{country.name}</span>
                          {country.population && (
                              <span className="country-pop">
                        {(country.population / 1000000).toFixed(1)}M
                      </span>
                          )}
                        </div>
                      </div>
                  ))}
                </div>
              </div>
          )}

          {/* COUNTRIES TAB */}
          {activeTab === 'countries' && (
              <div className="admin-section compact">
                <h2>Länder & Regionen verwalten</h2>

                <div className="form-section">
                  <h3>{editingCountry ? 'Land bearbeiten' : 'Neues Land'}</h3>
                  <form onSubmit={handleCountrySubmit} className="admin-form">
                    <div className="form-row">
                      <div className="form-group">
                        <label>Code (ISO)</label>
                        <input
                            type="text"
                            value={countryForm.code}
                            onChange={(e) => setCountryForm({ ...countryForm, code: e.target.value.toUpperCase() })}
                            placeholder="DE"
                            maxLength={2}
                            required
                            disabled={!!editingCountry}
                        />
                      </div>
                      <div className="form-group">
                        <label>Name (DE)</label>
                        <input
                            type="text"
                            value={countryForm.name_de}
                            onChange={(e) => setCountryForm({ ...countryForm, name_de: e.target.value })}
                            placeholder="Deutschland"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Name (EN)</label>
                        <input
                            type="text"
                            value={countryForm.name_en}
                            onChange={(e) => setCountryForm({ ...countryForm, name_en: e.target.value })}
                            placeholder="Germany"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Einwohner</label>
                        <input
                            type="number"
                            value={countryForm.population || ''}
                            onChange={(e) => setCountryForm({ ...countryForm, population: parseInt(e.target.value) || 0 })}
                            placeholder="83200000"
                            min="0"
                        />
                      </div>
                    </div>
                    <div className="form-actions">
                      <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? 'Speichert...' : (editingCountry ? 'Aktualisieren' : 'Anlegen')}
                      </button>
                      {editingCountry && (
                          <button
                              type="button"
                              className="btn-secondary"
                              onClick={() => {
                                setEditingCountry(null);
                                setCountryForm({ code: '', name_de: '', name_en: '', population: 0 });
                              }}
                          >
                            Abbrechen
                          </button>
                      )}
                    </div>
                  </form>
                </div>

                <div className="form-section">
                  <h3>Neue Region</h3>
                  <form onSubmit={handleRegionSubmit} className="admin-form">
                    <div className="form-row">
                      <div className="form-group">
                        <label>Land (PFLICHT)</label>
                        <select
                            value={regionForm.countryCode}
                            onChange={(e) => setRegionForm({ ...regionForm, countryCode: e.target.value })}
                            required
                        >
                          <option value="">-- Wählen --</option>
                          {countries.map(c => (
                              <option key={c.id} value={c.code}>{c.name}</option>
                          ))}
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Code (ISO)</label>
                        <input
                            type="text"
                            value={regionForm.code}
                            onChange={(e) => setRegionForm({ ...regionForm, code: e.target.value.toUpperCase() })}
                            placeholder="DE-BY"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Name (DE)</label>
                        <input
                            type="text"
                            value={regionForm.name_de}
                            onChange={(e) => setRegionForm({ ...regionForm, name_de: e.target.value })}
                            placeholder="Bayern"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Name (EN)</label>
                        <input
                            type="text"
                            value={regionForm.name_en}
                            onChange={(e) => setRegionForm({ ...regionForm, name_en: e.target.value })}
                            placeholder="Bavaria"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Einwohner</label>
                        <input
                            type="number"
                            value={regionForm.population || ''}
                            onChange={(e) => setRegionForm({ ...regionForm, population: parseInt(e.target.value) || 0 })}
                            placeholder="13100000"
                            min="0"
                            required
                        />
                      </div>
                    </div>
                    <button type="submit" className="btn-primary" disabled={loading}>
                      {loading ? 'Speichert...' : 'Anlegen'}
                    </button>
                  </form>
                </div>

                <div className="existing-data">
                  <h3>Bestehende Länder</h3>
                  {countries.length === 0 ? (
                      <p className="no-data">Noch keine Länder angelegt</p>
                  ) : (
                      <div className="data-table">
                        {countries.map(country => (
                            <div key={country.id} className="data-row">
                              <div className="data-main">
                                <span className="data-code">{country.code}</span>
                                <span className="data-name">{country.name}</span>
                                {country.population && (
                                    <span className="data-pop">
                            {(country.population / 1000000).toFixed(1)}M
                          </span>
                                )}
                              </div>
                              <div className="data-actions">
                                <button className="btn-edit" onClick={() => handleEditCountry(country)}>
                                  Bearbeiten
                                </button>
                                <button className="btn-delete" onClick={() => handleDeleteCountry(country.id)}>
                                  Löschen
                                </button>
                              </div>
                            </div>
                        ))}
                      </div>
                  )}
                </div>
              </div>
          )}

          {/* HOLIDAYS TAB */}
          {activeTab === 'holidays' && (
              <div className="admin-section compact">
                <h2>Feiertage & Ferien verwalten</h2>

                <div className="form-section">
                  <h3>{editingHoliday ? 'Bearbeiten' : 'Neu anlegen'}</h3>

                  <div className="toggle-row">
                    <div className="toggle-group">
                      <label className={holidayOrVacation === 'holiday' ? 'active' : ''}>
                        <input
                            type="radio"
                            value="holiday"
                            checked={holidayOrVacation === 'holiday'}
                            onChange={(e) => setHolidayOrVacation(e.target.value as HolidayOrVacation)}
                        />
                        Feiertag
                      </label>
                      <label className={holidayOrVacation === 'vacation' ? 'active' : ''}>
                        <input
                            type="radio"
                            value="vacation"
                            checked={holidayOrVacation === 'vacation'}
                            onChange={(e) => setHolidayOrVacation(e.target.value as HolidayOrVacation)}
                        />
                        Ferien
                      </label>
                    </div>

                    <div className="toggle-group">
                      <label className={scope === 'nationwide' ? 'active' : ''}>
                        <input
                            type="radio"
                            value="nationwide"
                            checked={scope === 'nationwide'}
                            onChange={(e) => setScope(e.target.value as Scope)}
                        />
                        Landesweit
                      </label>
                      <label className={scope === 'regional' ? 'active' : ''}>
                        <input
                            type="radio"
                            value="regional"
                            checked={scope === 'regional'}
                            onChange={(e) => setScope(e.target.value as Scope)}
                        />
                        Regional
                      </label>
                    </div>
                  </div>

                  <form onSubmit={handleHolidaySubmit} className="admin-form">
                    <div className="form-row">
                      <div className="form-group">
                        <label>Name (DE)</label>
                        <input
                            type="text"
                            value={holidayForm.name_de}
                            onChange={(e) => setHolidayForm({ ...holidayForm, name_de: e.target.value })}
                            placeholder={holidayOrVacation === 'holiday' ? 'Weihnachten' : 'Sommerferien'}
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Name (EN)</label>
                        <input
                            type="text"
                            value={holidayForm.name_en}
                            onChange={(e) => setHolidayForm({ ...holidayForm, name_en: e.target.value })}
                            placeholder={holidayOrVacation === 'holiday' ? 'Christmas' : 'Summer Holidays'}
                            required
                        />
                      </div>
                    </div>

                    {holidayOrVacation === 'holiday' ? (
                        <div className="form-row">
                          <div className="form-group">
                            <label>Datum</label>
                            <input
                                type="date"
                                value={holidayForm.date}
                                onChange={(e) => setHolidayForm({ ...holidayForm, date: e.target.value })}
                                required
                            />
                          </div>
                        </div>
                    ) : (
                        <div className="form-row">
                          <div className="form-group">
                            <label>Von</label>
                            <input
                                type="date"
                                value={holidayForm.startDate}
                                onChange={(e) => setHolidayForm({ ...holidayForm, startDate: e.target.value })}
                                required
                            />
                          </div>
                          <div className="form-group">
                            <label>Bis</label>
                            <input
                                type="date"
                                value={holidayForm.endDate}
                                onChange={(e) => setHolidayForm({ ...holidayForm, endDate: e.target.value })}
                                required
                            />
                          </div>
                        </div>
                    )}

                    <div className="form-row">
                      <div className="form-group">
                        <label>Land</label>
                        <select
                            value={holidayForm.countryCode}
                            onChange={(e) => setHolidayForm({ ...holidayForm, countryCode: e.target.value, regionCodes: [] })}
                            required
                        >
                          <option value="">-- Wählen --</option>
                          {countries.map(c => (
                              <option key={c.id} value={c.code}>{c.name}</option>
                          ))}
                        </select>
                      </div>
                    </div>

                    {scope === 'regional' && holidayForm.countryCode && getAvailableRegions().length > 0 && (
                        <div className="selection-section">
                          <label>Regionen (mehrere möglich)</label>
                          <div className="selection-grid">
                            {getAvailableRegions().map(region => (
                                <label key={region.code} className="checkbox-label">
                                  <input
                                      type="checkbox"
                                      checked={holidayForm.regionCodes.includes(region.code)}
                                      onChange={() => toggleRegionSelection(region.code)}
                                  />
                                  {region.name_de}
                                </label>
                            ))}
                          </div>
                        </div>
                    )}

                    <div className="form-actions">
                      <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? 'Speichert...' : 'Speichern'}
                      </button>
                      {editingHoliday && (
                          <button type="button" className="btn-secondary" onClick={resetHolidayForm}>
                            Abbrechen
                          </button>
                      )}
                    </div>
                  </form>
                </div>

                <div className="form-section">
                  <h3>Suchen</h3>
                  <div className="search-form">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Name oder Land..."
                        onKeyDown={(e) => e.key === 'Enter' && handleSearchHolidays()}
                    />
                    <button onClick={handleSearchHolidays} className="btn-primary" disabled={loading}>
                      Suchen
                    </button>
                  </div>

                  {searchResults.length > 0 && (
                      <div className="search-results">
                        {searchResults.map(holiday => (
                            <div key={holiday.id} className="result-row">
                              <div className="result-main">
                                <span className="result-name">{holiday.localName}</span>
                                <span className="result-date">{holiday.date}</span>
                                <span className="result-country">{holiday.countryCode}</span>
                                {holiday.region?.code && (
                                    <span className="result-region">{holiday.region.code}</span>
                                )}
                              </div>
                              <div className="result-actions">
                                <button className="btn-edit" onClick={() => handleEditHoliday(holiday)}>
                                  Bearbeiten
                                </button>
                                <button className="btn-delete" onClick={() => handleDeleteHoliday(holiday.id)}>
                                  Löschen
                                </button>
                              </div>
                            </div>
                        ))}
                      </div>
                  )}
                </div>
              </div>
          )}
        </div>
      </div>
  );
};

export default AdminPanel;