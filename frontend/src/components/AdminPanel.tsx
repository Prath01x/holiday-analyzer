import { useState, useEffect } from 'react';
import { api } from '../api';
import { Country, SubdivisionInfo } from '../types';
import AdminHeader from './AdminHeader';
import './AdminPanel.css';

interface Props {
  onBack: () => void;
}

type AdminTab = 'overview' | 'countries' | 'holidays';
type HolidayOrVacation = 'holiday' | 'vacation';
type Scope = 'nationwide' | 'regional';

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

interface RegionFromAPI {
  id: number;
  code: string;
  name: string;
  population: number;
  country?: {
    code: string;
    name: string;
  };
}

interface HolidayFromAPI {
  id: number;
  localName: string;
  date: string;
  countryCode: string;
}

const AdminPanel = ({ onBack }: Props) => {
  const [activeTab, setActiveTab] = useState<AdminTab>('overview');
  const [countries, setCountries] = useState<Country[]>([]);
  const [subdivisions, setSubdivisions] = useState<SubdivisionInfo[]>([]);
  const [holidays, setHolidays] = useState<HolidayFromAPI[]>([]);
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

  const [holidayOrVacation, setHolidayOrVacation] = useState<HolidayOrVacation>('holiday');
  const [scope, setScope] = useState<Scope>('nationwide');
  const [holidayForm, setHolidayForm] = useState({
    name_de: '',
    name_en: '',
    date: '',
    startDate: '',
    endDate: '',
    countryCode: '',
    regionCodes: [] as string[],
  });

  const [regionsByCountry, setRegionsByCountry] = useState<Record<string, SubdivisionInfo[]>>({});

  useEffect(() => {
    fetchCountries();
    fetchSubdivisions();
    fetchHolidays();
    fetchSchoolHolidays();
    loadRegionsGrouped();
  }, []);

  const loadRegionsGrouped = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/regions');

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      const allRegions: RegionFromAPI[] = await response.json();

      const grouped: Record<string, SubdivisionInfo[]> = {};

      allRegions.forEach((region) => {
        const countryCode = region.country?.code;

        if (!countryCode) {
          return;
        }

        if (!grouped[countryCode]) {
          grouped[countryCode] = [];
        }

        grouped[countryCode].push({
          code: region.code,
          name: region.name,
          countryCode: countryCode,
          population: region.population
        });
      });

      setRegionsByCountry(grouped);
    } catch (error) {
      console.error('Error loading regions:', error);
    }
  };

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

  // ==================== COUNTRY HANDLERS ====================

  const handleCountrySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const token = localStorage.getItem('token');

      if (editingCountry) {
        const params = new URLSearchParams({
          name: countryForm.name_de,
          nameEn: countryForm.name_en,
          population: countryForm.population.toString()
        });

        const response = await fetch(
            `http://localhost:8080/api/admin/countries/${editingCountry.id}?${params}`,
            {
              method: 'PUT',
              headers: { 'Authorization': `Bearer ${token}` }
            }
        );

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || 'Fehler beim Aktualisieren');
        }

        showMessage('success', `Land ${countryForm.name_de} erfolgreich aktualisiert`);
      } else {
        const params = new URLSearchParams({
          code: countryForm.code,
          name: countryForm.name_de,
          nameEn: countryForm.name_en,
          population: countryForm.population.toString()
        });

        const response = await fetch(
            `http://localhost:8080/api/admin/countries?${params}`,
            {
              method: 'POST',
              headers: { 'Authorization': `Bearer ${token}` }
            }
        );

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || 'Fehler beim Erstellen');
        }

        showMessage('success', `Land ${countryForm.name_de} erfolgreich erstellt`);
      }

      setCountryForm({ code: '', name_de: '', name_en: '', population: 0 });
      setEditingCountry(null);
      fetchCountries();
    } catch (error) {
      showMessage('error', 'Fehler: ' + error);
      console.error(error);
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
    if (!window.confirm('Land wirklich löschen? Alle zugehörigen Regionen und Feiertage werden auch gelöscht!')) {
      return;
    }

    try {
      const token = localStorage.getItem('token');

      const response = await fetch(
          `http://localhost:8080/api/admin/countries/${id}`,
          {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
          }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Fehler beim Löschen');
      }

      showMessage('success', 'Land erfolgreich gelöscht');
      fetchCountries();
      fetchSubdivisions();
      loadRegionsGrouped();
    } catch (error) {
      showMessage('error', 'Fehler: ' + error);
      console.error(error);
    }
  };

  // ==================== REGION HANDLERS ====================

  const handleRegionSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const token = localStorage.getItem('token');

      const params = new URLSearchParams({
        code: regionForm.code,
        name: regionForm.name_de,
        countryCode: regionForm.countryCode,
        population: regionForm.population.toString()
      });

      const response = await fetch(
          `http://localhost:8080/api/admin/regions?${params}`,
          {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
          }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Fehler beim Erstellen');
      }

      showMessage('success', `Region ${regionForm.name_de} erfolgreich erstellt`);
      setRegionForm({ countryCode: '', code: '', name_de: '', name_en: '', population: 0 });
      fetchSubdivisions();
      loadRegionsGrouped();
    } catch (error) {
      showMessage('error', 'Fehler: ' + error);
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  // ==================== HOLIDAY HANDLERS ====================

  const handleHolidaySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      let startDate: string;
      let endDate: string;

      if (holidayOrVacation === 'holiday') {
        startDate = holidayForm.date;
        endDate = holidayForm.date;
      } else {
        startDate = holidayForm.startDate;
        endDate = holidayForm.endDate;
      }

      const year = new Date(startDate).getFullYear();
      const token = localStorage.getItem('token');

      if (holidayOrVacation === 'vacation') {
        const regionCodes = scope === 'nationwide'
            ? getAvailableRegions().map(r => r.code)
            : holidayForm.regionCodes;

        if (regionCodes.length === 0) {
          showMessage('error', 'Keine Regionen ausgewählt!');
          setLoading(false);
          return;
        }

        for (const regionCode of regionCodes) {
          const queryParams = new URLSearchParams({
            name: holidayForm.name_de,
            regionCode: regionCode,
            startDate: startDate,
            endDate: endDate,
            year: year.toString()
          });

          const response = await fetch(
              `http://localhost:8080/api/admin/school-holidays?${queryParams}`,
              {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
              }
          );

          if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Fehler beim Speichern: ${response.status} - ${errorText}`);
          }
        }

        showMessage('success', `${regionCodes.length} Schulferien erfolgreich gespeichert`);
      } else {
        if (scope === 'nationwide') {
          const queryParams = new URLSearchParams({
            name: holidayForm.name_de,
            countryCode: holidayForm.countryCode,
            date: holidayForm.date,
            englishName: holidayForm.name_en
          });

          const response = await fetch(
              `http://localhost:8080/api/admin/holidays?${queryParams}`,
              {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
              }
          );

          if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Fehler: ${response.status} - ${errorText}`);
          }

          showMessage('success', 'Feiertag erfolgreich gespeichert');
        } else {
          const regionCodes = holidayForm.regionCodes;

          if (regionCodes.length === 0) {
            showMessage('error', 'Keine Regionen ausgewählt!');
            setLoading(false);
            return;
          }

          for (const regionCode of regionCodes) {
            const queryParams = new URLSearchParams({
              name: holidayForm.name_de,
              countryCode: holidayForm.countryCode,
              date: holidayForm.date,
              regionCode: regionCode,
              englishName: holidayForm.name_en
            });

            const response = await fetch(
                `http://localhost:8080/api/admin/holidays?${queryParams}`,
                {
                  method: 'POST',
                  headers: { 'Authorization': `Bearer ${token}` }
                }
            );

            if (!response.ok) {
              const errorText = await response.text();
              throw new Error(`Fehler: ${response.status} - ${errorText}`);
            }
          }

          showMessage('success', `${regionCodes.length} regionale Feiertage erfolgreich gespeichert`);
        }
      }

      resetHolidayForm();
      fetchHolidays();
      fetchSchoolHolidays();
    } catch (error) {
      showMessage('error', 'Fehler beim Speichern: ' + error);
      console.error('Fehler Details:', error);
    } finally {
      setLoading(false);
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
  };

  const toggleRegionSelection = (code: string) => {
    setHolidayForm(prev => ({
      ...prev,
      regionCodes: prev.regionCodes.includes(code)
          ? prev.regionCodes.filter(c => c !== code)
          : [...prev.regionCodes, code],
    }));
  };

  const getAvailableRegions = (): SubdivisionInfo[] => {
    if (!holidayForm.countryCode) return [];
    return regionsByCountry[holidayForm.countryCode] || [];
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

          {activeTab === 'holidays' && (
              <div className="admin-section compact">
                <h2>Feiertage & Ferien verwalten</h2>

                <div className="form-section">
                  <h3>Neu anlegen</h3>

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
                    </div>
                  </form>
                </div>
              </div>
          )}
        </div>
      </div>
  );
};

export default AdminPanel;