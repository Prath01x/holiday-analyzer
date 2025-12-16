import { useState, useEffect } from 'react';
import { api } from '../api';
import { Country, Holiday, SubdivisionInfo } from '../types';
import AdminHeader from './AdminHeader';
import './AdminPanel.css';

interface Props {
  onBack: () => void;
}

type AdminTab = 'overview' | 'countries' | 'holidays';
type HolidayType = 'public_holiday' | 'school_holiday';

interface MockRegion {
  code: string;
  name: string;
  countryCode: string;
}

const AdminPanel = ({ onBack }: Props) => {
  const [activeTab, setActiveTab] = useState<AdminTab>('overview');
  const [countries, setCountries] = useState<Country[]>([]);
  const [subdivisions] = useState<SubdivisionInfo[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const [countryForm, setCountryForm] = useState({
    code: '',
    name: '',
    population: 0,
  });
  const [regionForm, setRegionForm] = useState({
    countryCode: '',
    code: '',
    name: '',
    population: 0,
  });
  const [editingCountry, setEditingCountry] = useState<Country | null>(null);

  const [holidayType, setHolidayType] = useState<HolidayType>('public_holiday');
  const [holidayForm, setHolidayForm] = useState({
    name: '',
    date: '',
    startDate: '',
    endDate: '',
    countryCodes: [] as string[],
    regionCodes: [] as string[],
  });

  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState<Holiday[]>([]);
  const [editingHoliday, setEditingHoliday] = useState<Holiday | null>(null);

  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [csvType, setCsvType] = useState<'country' | 'region' | 'holiday'>('country');

  // Mock Regionen - später vom Backend laden
  const mockRegionsByCountry: Record<string, MockRegion[]> = {
    'DE': [
      { code: 'DE-BW', name: 'Baden-Württemberg', countryCode: 'DE' },
      { code: 'DE-BY', name: 'Bayern', countryCode: 'DE' },
      { code: 'DE-NW', name: 'Nordrhein-Westfalen', countryCode: 'DE' },
      { code: 'DE-HE', name: 'Hessen', countryCode: 'DE' },
      { code: 'DE-SN', name: 'Sachsen', countryCode: 'DE' },
    ],
    'AT': [
      { code: 'AT-1', name: 'Burgenland', countryCode: 'AT' },
      { code: 'AT-2', name: 'Kärnten', countryCode: 'AT' },
      { code: 'AT-9', name: 'Wien', countryCode: 'AT' },
    ],
    'FR': [
      { code: 'FR-IDF', name: 'Île-de-France', countryCode: 'FR' },
      { code: 'FR-PAC', name: 'Provence-Alpes-Côte d\'Azur', countryCode: 'FR' },
    ],
  };

  useEffect(() => {
    fetchCountries();
  }, []);

  const fetchCountries = async () => {
    try {
      const data = await api.getCountries();
      setCountries(data);
    } catch (error) {
      console.error('Error fetching countries:', error);
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
      showMessage('success', `Land ${countryForm.name} erfolgreich ${editingCountry ? 'aktualisiert' : 'erstellt'}`);
      setCountryForm({ code: '', name: '', population: 0 });
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
      // TODO: Backend Endpoint POST /api/admin/regions
      showMessage('success', `Region ${regionForm.name} erfolgreich erstellt`);
      setRegionForm({ countryCode: '', code: '', name: '', population: 0 });
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
      name: country.name,
      population: country.population || 0,
    });
    // Scroll zu Form
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDeleteCountry = async (id: number) => {
    if (!window.confirm('Land wirklich löschen? Alle zugehörigen Daten gehen verloren.')) {
      return;
    }

    try {
      // TODO: Backend Endpoint DELETE /api/admin/countries/{id}
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
      // TODO: Backend Endpoint POST /api/admin/holidays
      const data = {
        name: holidayForm.name,
        type: holidayType,
        date: holidayType === 'public_holiday' ? holidayForm.date : undefined,
        startDate: holidayType === 'school_holiday' ? holidayForm.startDate : undefined,
        endDate: holidayType === 'school_holiday' ? holidayForm.endDate : undefined,
        countryCodes: holidayForm.countryCodes,
        regionCodes: holidayForm.regionCodes,
      };
      console.log('Sending to backend:', data);

      showMessage('success', `${holidayType === 'public_holiday' ? 'Feiertag' : 'Ferien'} erfolgreich gespeichert`);
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
      name: holiday.localName,
      date: holiday.date,
      startDate: '',
      endDate: '',
      countryCodes: [holiday.countryCode],
      regionCodes: holiday.subdivisionCodes ? holiday.subdivisionCodes.split(',') : [],
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDeleteHoliday = async (holidayId: number) => {
    if (!window.confirm('Feiertag/Ferien wirklich löschen?')) {
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
      name: '',
      date: '',
      startDate: '',
      endDate: '',
      countryCodes: [],
      regionCodes: [],
    });
    setEditingHoliday(null);
  };

  // CSV Import

  const handleCSVImport = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!csvFile) {
      showMessage('error', 'Bitte CSV-Datei auswählen');
      return;
    }

    setLoading(true);

    try {
      // TODO: Backend CSV Import Endpoints
      // POST /api/admin/import/countries
      // POST /api/admin/import/regions
      // POST /api/admin/import/holidays

      showMessage('success', `CSV erfolgreich importiert: ${csvFile.name}`);
      setCsvFile(null);

      if (csvType === 'country') {
        fetchCountries();
      }
    } catch (error) {
      showMessage('error', 'Fehler beim CSV-Import');
    } finally {
      setLoading(false);
    }
  };

  // Selection Helpers

  const toggleCountrySelection = (code: string) => {
    setHolidayForm(prev => ({
      ...prev,
      countryCodes: prev.countryCodes.includes(code)
          ? prev.countryCodes.filter(c => c !== code)
          : [...prev.countryCodes, code],
      // Reset regions wenn Land abgewählt wird
      regionCodes: prev.countryCodes.includes(code)
          ? prev.regionCodes.filter(r => !r.startsWith(code))
          : prev.regionCodes,
    }));
  };

  const toggleRegionSelection = (code: string) => {
    setHolidayForm(prev => ({
      ...prev,
      regionCodes: prev.regionCodes.includes(code)
          ? prev.regionCodes.filter(c => c !== code)
          : [...prev.regionCodes, code],
    }));
  };

  const getAvailableRegions = (): MockRegion[] => {
    const regions: MockRegion[] = [];
    holidayForm.countryCodes.forEach(countryCode => {
      const countryRegions = mockRegionsByCountry[countryCode] || [];
      regions.push(...countryRegions);
    });
    return regions;
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
                    <div className="stat-number">0</div>
                    <div className="stat-label">Feiertage</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-number">0</div>
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
                        {(country.population / 1000000).toFixed(1)}M Einwohner
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
              <div className="admin-section">
                <h2>Länder & Regionen verwalten</h2>

                <div className="form-section">
                  <h3>{editingCountry ? 'Land bearbeiten' : 'Neues Land anlegen'}</h3>
                  <form onSubmit={handleCountrySubmit} className="admin-form">
                    <div className="form-row">
                      <div className="form-group">
                        <label>Ländercode (ISO)</label>
                        <input
                            type="text"
                            value={countryForm.code}
                            onChange={(e) => setCountryForm({ ...countryForm, code: e.target.value.toUpperCase() })}
                            placeholder="z.B. DE"
                            maxLength={5}
                            required
                            disabled={!!editingCountry}
                        />
                      </div>
                      <div className="form-group">
                        <label>Name</label>
                        <input
                            type="text"
                            value={countryForm.name}
                            onChange={(e) => setCountryForm({ ...countryForm, name: e.target.value })}
                            placeholder="z.B. Deutschland"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Einwohnerzahl</label>
                        <input
                            type="number"
                            value={countryForm.population || ''}
                            onChange={(e) => setCountryForm({ ...countryForm, population: parseInt(e.target.value) || 0 })}
                            placeholder="z.B. 83200000"
                            min="0"
                        />
                      </div>
                    </div>
                    <div className="form-actions">
                      <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? 'Speichert...' : (editingCountry ? 'Aktualisieren' : 'Land anlegen')}
                      </button>
                      {editingCountry && (
                          <button
                              type="button"
                              className="btn-secondary"
                              onClick={() => {
                                setEditingCountry(null);
                                setCountryForm({ code: '', name: '', population: 0 });
                              }}
                          >
                            Abbrechen
                          </button>
                      )}
                    </div>
                  </form>
                </div>

                <div className="form-section">
                  <h3>Region anlegen</h3>
                  <form onSubmit={handleRegionSubmit} className="admin-form">
                    <div className="form-row">
                      <div className="form-group">
                        <label>Land</label>
                        <select
                            value={regionForm.countryCode}
                            onChange={(e) => setRegionForm({ ...regionForm, countryCode: e.target.value })}
                            required
                        >
                          <option value="">-- Bitte wählen --</option>
                          {countries.map(c => (
                              <option key={c.id} value={c.code}>{c.name}</option>
                          ))}
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Regionscode (ISO 3166-2)</label>
                        <input
                            type="text"
                            value={regionForm.code}
                            onChange={(e) => setRegionForm({ ...regionForm, code: e.target.value.toUpperCase() })}
                            placeholder="z.B. DE-BY"
                            required
                        />
                      </div>
                      <div className="form-group">
                        <label>Name</label>
                        <input
                            type="text"
                            value={regionForm.name}
                            onChange={(e) => setRegionForm({ ...regionForm, name: e.target.value })}
                            placeholder="z.B. Bayern"
                            required
                        />
                      </div>
                    </div>
                    <div className="form-row">
                      <div className="form-group">
                        <label>Einwohnerzahl</label>
                        <input
                            type="number"
                            value={regionForm.population || ''}
                            onChange={(e) => setRegionForm({ ...regionForm, population: parseInt(e.target.value) || 0 })}
                            placeholder="z.B. 13100000"
                            min="0"
                            required
                        />
                      </div>
                    </div>
                    <button type="submit" className="btn-primary" disabled={loading}>
                      {loading ? 'Speichert...' : 'Region anlegen'}
                    </button>
                  </form>
                </div>

                <div className="form-section">
                  <h3>CSV Import</h3>
                  <form onSubmit={handleCSVImport} className="csv-form">
                    <div className="form-group">
                      <label>Import-Typ</label>
                      <select value={csvType} onChange={(e) => setCsvType(e.target.value as 'country' | 'region' | 'holiday')}>
                        <option value="country">Länder</option>
                        <option value="region">Regionen</option>
                      </select>
                    </div>
                    <div className="form-group">
                      <label>CSV-Datei</label>
                      <input
                          type="file"
                          accept=".csv"
                          onChange={(e) => setCsvFile(e.target.files?.[0] || null)}
                      />
                    </div>
                    <button type="submit" className="btn-secondary" disabled={loading || !csvFile}>
                      {loading ? 'Importiert...' : 'CSV Importieren'}
                    </button>
                  </form>
                  <div className="csv-format-info">
                    <strong>Format für Länder:</strong> code,name,population<br/>
                    <strong>Format für Regionen:</strong> countryCode,code,name,population
                  </div>
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
                                <button
                                    className="btn-edit"
                                    onClick={() => handleEditCountry(country)}
                                >
                                  Bearbeiten
                                </button>
                                <button
                                    className="btn-delete"
                                    onClick={() => handleDeleteCountry(country.id)}
                                >
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
              <div className="admin-section">
                <h2>Feiertage & Ferien verwalten</h2>

                <div className="form-section">
                  <h3>{editingHoliday ? 'Bearbeiten' : 'Neu anlegen'}</h3>

                  <div className="type-selector">
                    <label>
                      <input
                          type="radio"
                          value="public_holiday"
                          checked={holidayType === 'public_holiday'}
                          onChange={(e) => setHolidayType(e.target.value as HolidayType)}
                      />
                      Feiertag
                    </label>
                    <label>
                      <input
                          type="radio"
                          value="school_holiday"
                          checked={holidayType === 'school_holiday'}
                          onChange={(e) => setHolidayType(e.target.value as HolidayType)}
                      />
                      Ferien
                    </label>
                  </div>

                  <form onSubmit={handleHolidaySubmit} className="admin-form">
                    <div className="form-group">
                      <label>Name</label>
                      <input
                          type="text"
                          value={holidayForm.name}
                          onChange={(e) => setHolidayForm({ ...holidayForm, name: e.target.value })}
                          placeholder={holidayType === 'public_holiday' ? 'z.B. Weihnachten' : 'z.B. Sommerferien'}
                          required
                      />
                    </div>

                    {holidayType === 'public_holiday' ? (
                        <div className="form-group">
                          <label>Datum</label>
                          <input
                              type="date"
                              value={holidayForm.date}
                              onChange={(e) => setHolidayForm({ ...holidayForm, date: e.target.value })}
                              required
                          />
                        </div>
                    ) : (
                        <div className="form-row">
                          <div className="form-group">
                            <label>Startdatum</label>
                            <input
                                type="date"
                                value={holidayForm.startDate}
                                onChange={(e) => setHolidayForm({ ...holidayForm, startDate: e.target.value })}
                                required
                            />
                          </div>
                          <div className="form-group">
                            <label>Enddatum</label>
                            <input
                                type="date"
                                value={holidayForm.endDate}
                                onChange={(e) => setHolidayForm({ ...holidayForm, endDate: e.target.value })}
                                required
                            />
                          </div>
                        </div>
                    )}

                    <div className="selection-section">
                      <label>Betroffene Länder (mehrere möglich)</label>
                      <div className="selection-grid">
                        {countries.map(country => (
                            <label key={country.id} className="checkbox-label">
                              <input
                                  type="checkbox"
                                  checked={holidayForm.countryCodes.includes(country.code)}
                                  onChange={() => toggleCountrySelection(country.code)}
                              />
                              {country.name}
                            </label>
                        ))}
                      </div>
                    </div>

                    {holidayForm.countryCodes.length > 0 && getAvailableRegions().length > 0 && (
                        <div className="selection-section">
                          <label>Betroffene Regionen (optional, mehrere möglich)</label>
                          <div className="selection-grid">
                            {getAvailableRegions().map(region => (
                                <label key={region.code} className="checkbox-label">
                                  <input
                                      type="checkbox"
                                      checked={holidayForm.regionCodes.includes(region.code)}
                                      onChange={() => toggleRegionSelection(region.code)}
                                  />
                                  {region.name}
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
                  <h3>Feiertage/Ferien suchen</h3>
                  <div className="search-form">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Nach Name oder Land suchen..."
                        onKeyDown={(e) => e.key === 'Enter' && handleSearchHolidays()}
                    />
                    <button onClick={handleSearchHolidays} className="btn-primary" disabled={loading}>
                      Suchen
                    </button>
                  </div>

                  {searchResults.length > 0 && (
                      <div className="search-results">
                        <h4>Suchergebnisse ({searchResults.length})</h4>
                        {searchResults.map(holiday => (
                            <div key={holiday.id} className="result-row">
                              <div className="result-main">
                                <span className="result-name">{holiday.localName}</span>
                                <span className="result-date">{holiday.date}</span>
                                <span className="result-country">{holiday.countryCode}</span>
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

                <div className="form-section">
                  <h3>CSV Import</h3>
                  <form onSubmit={handleCSVImport} className="csv-form">
                    <div className="form-group">
                      <label>CSV-Datei</label>
                      <input
                          type="file"
                          accept=".csv"
                          onChange={(e) => {
                            setCsvFile(e.target.files?.[0] || null);
                            setCsvType('holiday');
                          }}
                      />
                    </div>
                    <button type="submit" className="btn-secondary" disabled={loading || !csvFile}>
                      {loading ? 'Importiert...' : 'CSV Importieren'}
                    </button>
                  </form>
                  <div className="csv-format-info">
                    <strong>Format:</strong> type,name,date/startDate,endDate,countryCode,regionCodes
                  </div>
                </div>
              </div>
          )}
        </div>
      </div>
  );
};

export default AdminPanel;