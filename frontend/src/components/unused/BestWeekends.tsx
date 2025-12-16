import { WeekendAnalysis } from '../../types.ts';
import './BestWeekends.css';

interface Props {
  weekends: WeekendAnalysis[];
  selectedCountries: string[];
}

const BestWeekends = ({ weekends, selectedCountries }: Props) => {
  const getLoadLevelClass = (loadPercentage: number): string => {
    if (loadPercentage < 10) return 'very-low';
    if (loadPercentage < 25) return 'low';
    if (loadPercentage < 50) return 'medium';
    if (loadPercentage < 75) return 'high';
    return 'very-high';
  };

  const getLoadLevelText = (loadPercentage: number): string => {
    if (loadPercentage < 10) return 'Sehr niedrig';
    if (loadPercentage < 25) return 'Niedrig';
    if (loadPercentage < 50) return 'Mittel';
    if (loadPercentage < 75) return 'Hoch';
    return 'Sehr hoch';
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('de-DE', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const getFilterText = (): string => {
    if (selectedCountries.length === 0) {
      return 'Alle Länder';
    }
    return selectedCountries.join(', ');
  };

  return (
      <div className="best-weekends">
        <div className="weekends-header">
          <h2>Beste Wochenenden</h2>
          <p className="weekends-subtitle">
            Top 10 Wochenenden mit geringster Auslastung
          </p>
          {selectedCountries.length > 0 && (
              <div className="filter-indicator">
                <span className="filter-icon">🔍</span>
                Filter: {getFilterText()}
              </div>
          )}
        </div>

        {weekends.length === 0 ? (
            <div className="no-weekends">
              <p>Keine Daten verfügbar</p>
            </div>
        ) : (
            <div className="weekends-list">
              {weekends.map((weekend, index) => (
                  <div key={`${weekend.startDate}-${weekend.endDate}-${index}`} className="weekend-item">
                    <div className="weekend-rank">#{index + 1}</div>

                    <div className="weekend-content">
                      <div className="weekend-dates">
                  <span className="date-range">
                    {formatDate(weekend.startDate)} - {formatDate(weekend.endDate)}
                  </span>
                        {weekend.countryCode && (
                            <span className="weekend-country">{weekend.countryCode}</span>
                        )}
                      </div>

                      <div className="weekend-load">
                        <div className="load-bar-container">
                          <div
                              className={`load-bar load-${getLoadLevelClass(weekend.loadPercentage)}`}
                              style={{ width: `${weekend.loadPercentage}%` }}
                          />
                        </div>
                        <div className="load-info">
                          <span className="load-percentage">{weekend.loadPercentage.toFixed(1)}%</span>
                          <span className={`load-level load-level-${getLoadLevelClass(weekend.loadPercentage)}`}>
                      {getLoadLevelText(weekend.loadPercentage)}
                    </span>
                        </div>
                      </div>

                      {weekend.affectedRegions && weekend.affectedRegions.length > 0 && (
                          <div className="weekend-regions">
                            <span className="regions-label">Betroffene Regionen:</span>
                            <span className="regions-list">
                      {weekend.affectedRegions.join(', ')}
                    </span>
                          </div>
                      )}
                    </div>
                  </div>
              ))}
            </div>
        )}

        <div className="weekends-legend">
          <h4>Auslastung</h4>
          <div className="legend-items">
            <div className="legend-item">
              <span className="legend-color very-low"></span>
              <span>Sehr niedrig (&lt;10%)</span>
            </div>
            <div className="legend-item">
              <span className="legend-color low"></span>
              <span>Niedrig (10-25%)</span>
            </div>
            <div className="legend-item">
              <span className="legend-color medium"></span>
              <span>Mittel (25-50%)</span>
            </div>
            <div className="legend-item">
              <span className="legend-color high"></span>
              <span>Hoch (50-75%)</span>
            </div>
            <div className="legend-item">
              <span className="legend-color very-high"></span>
              <span>Sehr hoch (≥75%)</span>
            </div>
          </div>
        </div>
      </div>
  );
};

export default BestWeekends;