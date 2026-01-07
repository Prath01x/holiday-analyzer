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

    // Sammle alle Schulferien im Zeitraum (dedupliziert)
    const schoolHolidaysInRange = Array.from(
        new Map(
            daysInRange
                .filter(day => day.schoolHolidays && day.schoolHolidays.length > 0)
                .flatMap(day => day.schoolHolidays || [])
                .map(sh => [sh.id, sh])
        ).values()
    );

    // Gruppiere Feiertage nach Land/Region
    const groupedHolidays: { [key: string]: any[] } = {};

    holidaysInRange.forEach(holiday => {
        const key = holiday.globalHoliday
            ? holiday.countryCode
            : `${holiday.countryCode}-${holiday.region?.code}`;

        if (!groupedHolidays[key]) {
            groupedHolidays[key] = [];
        }
        groupedHolidays[key].push(holiday);
    });

    // Gruppiere Schulferien nach Region
    const groupedSchoolHolidays: { [key: string]: any[] } = {};

    schoolHolidaysInRange.forEach(schoolHoliday => {
        const key = schoolHoliday.region.code;

        if (!groupedSchoolHolidays[key]) {
            groupedSchoolHolidays[key] = [];
        }
        groupedSchoolHolidays[key].push(schoolHoliday);
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
            'IT': 'Italien',
            'ES': 'Spanien',
            'NL': 'Niederlande',
            'PL': 'Polen',
            'NO': 'Norwegen'
        };
        return countries[code] || code;
    };

    const getRegionPopulation = (region: { code: string; population?: number } | null | undefined): number => {
        if (!region) return 0;

        // Use region population if available
        if (region.population) return region.population;

        return 0;
    };

    const getCountryPopulation = (code: string): number => {
        const populations: { [key: string]: number } = {
            'DE': 83200000,
            'AT': 8900000,
            'CH': 8700000,
            'FR': 67400000,
            'IT': 59000000,
            'ES': 47900000,
            'NL': 17500000,
            'PL': 38500000,
            'NO': 5600000
        };
        return populations[code] || 0;
    };

    return (
        <div className="selected-range-info">
            <h3>Feiertage & Ferien im Zeitraum</h3>
            <div className="range-display">
                <strong>{formatDate(startDate)}</strong> bis <strong>{formatDate(endDate)}</strong>
            </div>

            {/* FEIERTAGE SECTION */}
            {holidaysInRange.length === 0 ? (
                <div className="no-holidays">
                    <p>Keine Feiertage in diesem Zeitraum</p>
                </div>
            ) : (
                <div className="holidays-section">
                    <h4 className="section-title">🎉 Feiertage</h4>
                    <div className="holidays-list">
                        {Object.entries(groupedHolidays).map(([key, holidays]) => {
                            const firstHoliday = holidays[0];
                            const isGlobal = firstHoliday.globalHoliday;
                            const countryCode = firstHoliday.countryCode;
                            const population = isGlobal
                                ? getCountryPopulation(countryCode)
                                : getRegionPopulation(firstHoliday.region);

                            return (
                                <div key={key} className="holiday-group">
                                    <div className="holiday-group-header">
                                        <div className="location-info">
                                            <span className="country-flag">{countryCode}</span>
                                            <div className="location-details">
                                                <strong className="location-name">
                                                    {isGlobal ? getCountryName(countryCode) : firstHoliday.region?.name || firstHoliday.region?.code}
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
                </div>
            )}

            {/* SCHULFERIEN SECTION */}
            {schoolHolidaysInRange.length > 0 && (
                <div className="school-holidays-section">
                    <h4 className="section-title">📚 Schulferien</h4>
                    <div className="holidays-list">
                        {Object.entries(groupedSchoolHolidays).map(([regionCode, schoolHolidays]) => {
                            const firstSchoolHoliday = schoolHolidays[0];
                            const region = firstSchoolHoliday.region;
                            const population = region.population || 0;

                            return (
                                <div key={regionCode} className="holiday-group">
                                    <div className="holiday-group-header">
                                        <div className="location-info">
                                            <span className="country-flag">{regionCode.split('-')[0]}</span>
                                            <div className="location-details">
                                                <strong className="location-name">
                                                    {region.name}
                                                </strong>
                                                <span className="location-type">
                                                    Regional
                                                </span>
                                            </div>
                                        </div>
                                        <div className="population-badge">
                                            {formatPopulation(population)} Einwohner
                                        </div>
                                    </div>

                                    <div className="holidays-in-group">
                                        {schoolHolidays.map((schoolHoliday) => (
                                            <div key={schoolHoliday.id} className="holiday-item">
                                                <div className="holiday-date">
                                                    {formatDate(schoolHoliday.startDate)} - {formatDate(schoolHoliday.endDate)}
                                                </div>
                                                <div className="holiday-name">
                                                    {schoolHoliday.name}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            )}

            <div className="range-stats">
                <div className="stat-item">
                    <span className="stat-label">Feiertage:</span>
                    <span className="stat-value">{holidaysInRange.length}</span>
                </div>
                <div className="stat-item">
                    <span className="stat-label">Schulferien:</span>
                    <span className="stat-value">{schoolHolidaysInRange.length}</span>
                </div>
                <div className="stat-item">
                    <span className="stat-label">Betroffene Regionen:</span>
                    <span className="stat-value">{Object.keys(groupedHolidays).length + Object.keys(groupedSchoolHolidays).length}</span>
                </div>
            </div>
        </div>
    );
};

export default SelectedRangeInfo;