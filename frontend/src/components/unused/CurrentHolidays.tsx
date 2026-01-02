import { UpcomingHoliday } from '../../types';
import './CurrentHolidays.css';

interface Props {
  holidays: UpcomingHoliday[];
  selectedCountries: string[];
}

const CurrentHolidays = ({ holidays, selectedCountries }: Props) => {
  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const today = new Date();
    const diffTime = date.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    const formatted = date.toLocaleDateString('de-DE', {
      weekday: 'short',
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });

    if (diffDays === 0) return `${formatted} (Heute)`;
    if (diffDays === 1) return `${formatted} (Morgen)`;
    if (diffDays > 0 && diffDays <= 7) return `${formatted} (in ${diffDays} Tagen)`;
    return formatted;
  };

  const getFilterText = (): string => {
    if (selectedCountries.length === 0) {
      return 'Alle Länder';
    }
    return selectedCountries.join(', ');
  };

  const formatPopulation = (population: number): string => {
    if (population >= 1000000) {
      return `${(population / 1000000).toFixed(1)}M`;
    }
    if (population >= 1000) {
      return `${(population / 1000).toFixed(0)}K`;
    }
    return population.toString();
  };

  return (
      <div className="current-holidays">
        <div className="holidays-header">
          <h2>Kommende Feiertage</h2>
          <p className="holidays-subtitle">
            Nächste 4 Wochen
          </p>
          {selectedCountries.length > 0 && (
              <div className="filter-indicator">
                <span className="filter-icon">🔍</span>
                Filter: {getFilterText()}
              </div>
          )}
        </div>

        {holidays.length === 0 ? (
            <div className="no-holidays">
              <p>Keine Feiertage in den nächsten 4 Wochen</p>
            </div>
        ) : (
            <div className="holidays-list">
              {holidays.map((holiday, index) => (
                  <div key={`${holiday.date}-${holiday.countryCode}-${index}`} className="holiday-item">
                    <div className="holiday-date">
                      <div className="date-day">
                        {new Date(holiday.date).toLocaleDateString('de-DE', { day: '2-digit' })}
                      </div>
                      <div className="date-month">
                        {new Date(holiday.date).toLocaleDateString('de-DE', { month: 'short' })}
                      </div>
                    </div>

                    <div className="holiday-content">
                      <div className="holiday-name">
                        {holiday.name}
                        {holiday.isGlobal && (
                            <span className="global-badge" title="Landesweit">🌍</span>
                        )}
                      </div>

                      <div className="holiday-meta">
                        <span className="holiday-country">{holiday.countryCode}</span>

                        {holiday.regions && holiday.regions.length > 0 && (
                            <span className="holiday-regions">
                      {holiday.regions.join(', ')}
                    </span>
                        )}

                        <span className="holiday-population">
                    {formatPopulation(holiday.affectedPopulation)} Einwohner
                  </span>
                      </div>

                      <div className="holiday-date-full">
                        {formatDate(holiday.date)}
                      </div>
                    </div>
                  </div>
              ))}
            </div>
        )}

        <div className="holidays-footer">
        <span className="footer-note">
          Sortiert nach Datum und betroffener Bevölkerung
        </span>
        </div>
      </div>
  );
};

export default CurrentHolidays;