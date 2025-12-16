import { DayAnalysis } from '../types';
import './SelectedRangeInfo.css';

interface Props {
    startDate: string;
    endDate: string;
    dayAnalyses: DayAnalysis[];
}

const SelectedRangeInfo = ({ startDate, endDate, dayAnalyses }: Props) => {
    if (!startDate || !endDate) {
        return null;
    }

    // Filtere Tage im ausgewählten Zeitraum
    const daysInRange = dayAnalyses.filter(day =>
        day.date >= startDate && day.date <= endDate
    );

    // Sammle alle Feiertage im Zeitraum
    const holidaysInRange = daysInRange
        .filter(day => day.holidays && day.holidays.length > 0)
        .flatMap(day => day.holidays.map(holiday => ({ ...holiday, date: day.date })));

    // Gruppiere nach Land/Region
    const groupedHolidays: { [key: string]: any[] } = {};

    holidaysInRange.forEach(holiday => {
        const key = holiday.globalHoliday
            ? holiday.countryCode
            : `${holiday.countryCode}-${holiday.subdivisionCodes}`;

        if (!groupedHolidays[key]) {
            groupedHolidays[key] = [];
        }
        groupedHolidays[key].push(holiday);
    });

    const formatDate = (dateString: string): string => {
        const date = new Date(dateString);
        return date.toLocaleDateString('de-DE', {
            weekday: 'short',
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        });
    };

    const formatPopulation = (pop: number): string => {
        if (pop >= 1000000) {
            return `${(pop / 1000000).toFixed(1)}M`;
        }
        if (pop >= 1000) {
            return `${(pop / 1000).toFixed(0)}K`;
        }
        return pop.toString();
    };

    const getCountryName = (code: string): string => {
        const countries: { [key: string]: string } = {
            'DE': 'Deutschland',
            'AT': 'Österreich',
            'CH': 'Schweiz',
            'FR': 'Frankreich',
            'IT': 'Italien'
        };
        return countries[code] || code;
    };

    const getRegionPopulation = (subdivisionCodes: string | null): number => {
        // Mock Populations - später aus API
        const populations: { [key: string]: number } = {
            'DE-BW': 11100000,
            'DE-BY': 13100000,
            'DE-NW': 17900000,
            'DE-HE': 6300000,
            'AT-1': 300000,
            'AT-2': 560000,
            'AT-9': 1900000,
        };

        if (!subdivisionCodes) return 0;

        return subdivisionCodes.split(',').reduce((sum, code) => {
            return sum + (populations[code.trim()] || 0);
        }, 0);
    };

    const getCountryPopulation = (code: string): number => {
        const populations: { [key: string]: number } = {
            'DE': 83200000,
            'AT': 8900000,
            'CH': 8700000,
            'FR': 67400000,
            'IT': 59000000
        };
        return populations[code] || 0;
    };

    return (
        <div className="selected-range-info">
            <h3>Feiertage im Zeitraum</h3>
            <div className="range-display">
                <strong>{formatDate(startDate)}</strong> bis <strong>{formatDate(endDate)}</strong>
            </div>

            {holidaysInRange.length === 0 ? (
                <div className="no-holidays">
                    <p>Keine Feiertage in diesem Zeitraum</p>
                </div>
            ) : (
                <div className="holidays-list">
                    {Object.entries(groupedHolidays).map(([key, holidays]) => {
                        const firstHoliday = holidays[0];
                        const isGlobal = firstHoliday.globalHoliday;
                        const countryCode = firstHoliday.countryCode;
                        const population = isGlobal
                            ? getCountryPopulation(countryCode)
                            : getRegionPopulation(firstHoliday.subdivisionCodes);

                        return (
                            <div key={key} className="holiday-group">
                                <div className="holiday-group-header">
                                    <div className="location-info">
                                        <span className="country-flag">{countryCode}</span>
                                        <div className="location-details">
                                            <strong className="location-name">
                                                {isGlobal ? getCountryName(countryCode) : firstHoliday.subdivisionCodes}
                                            </strong>
                                            <span className="location-type">
                        {isGlobal ? 'Landesweit' : 'Regional'}
                      </span>
                                        </div>
                                    </div>
                                    <div className="population-badge">
                                        {formatPopulation(population)} Einwohner
                                    </div>
                                </div>

                                <div className="holidays-in-group">
                                    {holidays.map((holiday, index) => (
                                        <div key={`${holiday.date}-${index}`} className="holiday-item">
                                            <div className="holiday-date">
                                                {formatDate(holiday.date)}
                                            </div>
                                            <div className="holiday-name">
                                                {holiday.localName}
                                            </div>
                                            {holiday.englishName && holiday.englishName !== holiday.localName && (
                                                <div className="holiday-name-en">
                                                    {holiday.englishName}
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}

            <div className="range-stats">
                <div className="stat-item">
                    <span className="stat-label">Gesamt Feiertage:</span>
                    <span className="stat-value">{holidaysInRange.length}</span>
                </div>
                <div className="stat-item">
                    <span className="stat-label">Betroffene Länder/Regionen:</span>
                    <span className="stat-value">{Object.keys(groupedHolidays).length}</span>
                </div>
            </div>
        </div>
    );
};

export default SelectedRangeInfo;