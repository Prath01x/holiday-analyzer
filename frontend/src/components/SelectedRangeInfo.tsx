import { DayAnalysis } from '../types';
import './SelectedRangeInfo.css';

interface Props {
    startDate: string;
    endDate: string;
    dayAnalyses: DayAnalysis[];
}

interface GroupedSchoolHoliday {
    name: string;
    startDate: string;
    endDate: string;
    regions: Array<{
        code: string;
        name: string;
        population: number;
        countryCode: string;
    }>;
}

interface GroupedHoliday {
    date: string;
    name: string;
    regions: Array<{
        code: string;
        name: string;
        population: number;
        countryCode: string;
        isGlobal: boolean;
    }>;
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

    // HILFSFUNKTIONEN
    const formatDate = (dateString: string): string => {
        const date = new Date(dateString);
        return date.toLocaleDateString('de-DE', {
            weekday: 'short',
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        });
    };

    const formatDateShort = (dateString: string): string => {
        const date = new Date(dateString);
        return date.toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit'
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
        if (region.population) return region.population;
        return 0;
    };

    const getCountryPopulation = (code: string): number => {
        const populations: { [key: string]: number } = {
            'DE': 84552242,
            'AT': 9120813,
            'CH': 9048900,
            'FR': 66548530,
            'IT': 59342867,
            'ES': 47910526,
            'NL': 18228742,
            'PL': 38539201,
            'NO': 5576660
        };
        return populations[code] || 0;
    };

    const getTotalAffectedPopulation = (): number => {
        // Berechne einzigartige Regionen NUR aus Schulferien
        const uniqueRegions = new Map<string, number>();

        schoolHolidaysInRange.forEach(sh => {
            uniqueRegions.set(sh.region.code, sh.region.population);
        });

        return Array.from(uniqueRegions.values()).reduce((sum, pop) => sum + pop, 0);
    };

    // Ermittle Hauptland (das Land mit den meisten Regionen)
    const getMainCountry = (): { code: string; population: number } => {
        const countryCounts = new Map<string, number>();

        schoolHolidaysInRange.forEach(sh => {
            const countryCode = sh.region.code.split('-')[0];
            countryCounts.set(countryCode, (countryCounts.get(countryCode) || 0) + 1);
        });

        let mainCountry = 'DE';
        let maxCount = 0;
        countryCounts.forEach((count, country) => {
            if (count > maxCount) {
                maxCount = count;
                mainCountry = country;
            }
        });

        return {
            code: mainCountry,
            population: getCountryPopulation(mainCountry)
        };
    };

    // JETZT ERST DIE GRUPPIERUNG - NACH DEN FUNKTIONEN!
    // Gruppiere Schulferien nach Name + Zeitraum
    const groupedSchoolHolidays: GroupedSchoolHoliday[] = [];

    schoolHolidaysInRange.forEach(sh => {
        let group = groupedSchoolHolidays.find(
            g => g.name === sh.name && g.startDate === sh.startDate && g.endDate === sh.endDate
        );

        if (!group) {
            group = {
                name: sh.name,
                startDate: sh.startDate,
                endDate: sh.endDate,
                regions: []
            };
            groupedSchoolHolidays.push(group);
        }

        const regionCode = sh.region.code.split('-')[0];
        group.regions.push({
            code: sh.region.code,
            name: sh.region.name,
            population: sh.region.population,
            countryCode: regionCode
        });
    });

    // Sortiere Regionen innerhalb jeder Gruppe nach Population (größte zuerst)
    groupedSchoolHolidays.forEach(group => {
        group.regions.sort((a, b) => b.population - a.population);
    });

    // Gruppiere Feiertage nach Datum + Name
    const groupedHolidaysCompact: GroupedHoliday[] = [];

    holidaysInRange.forEach(holiday => {
        let group = groupedHolidaysCompact.find(
            g => g.date === holiday.date && g.name === holiday.localName
        );

        if (!group) {
            group = {
                date: holiday.date,
                name: holiday.localName,
                regions: []
            };
            groupedHolidaysCompact.push(group);
        }

        const countryCode = holiday.countryCode;

        if (holiday.globalHoliday) {
            // Bundesweiter Feiertag
            group.regions.push({
                code: countryCode,
                name: getCountryName(countryCode),
                population: getCountryPopulation(countryCode),
                countryCode: countryCode,
                isGlobal: true
            });
        } else if (holiday.region) {
            // Regionaler Feiertag
            group.regions.push({
                code: holiday.region.code,
                name: holiday.region.name,
                population: getRegionPopulation(holiday.region),
                countryCode: countryCode,
                isGlobal: false
            });
        }
    });

    // Sortiere Regionen innerhalb jeder Gruppe nach Population
    groupedHolidaysCompact.forEach(group => {
        group.regions.sort((a, b) => b.population - a.population);
    });

    const mainCountry = getMainCountry();
    const totalAffectedPop = getTotalAffectedPopulation();
    const percentageAffected = mainCountry.population > 0
        ? Math.round((totalAffectedPop / mainCountry.population) * 100)
        : 0;

    return (
        <div className="selected-range-info">
            <h3>Feiertage & Ferien im Zeitraum</h3>
            <div className="range-display">
                <strong>{formatDate(startDate)}</strong> bis <strong>{formatDate(endDate)}</strong>
            </div>

            {/* STATISTIK OBEN - 3 CARDS */}
            <div className="range-stats-top">
                <div className="stat-card stat-holidays">
                    <div className="stat-content">
                        <div className="stat-value">{holidaysInRange.length}</div>
                        <div className="stat-label">Feiertage</div>
                    </div>
                </div>
                <div className="stat-card stat-vacations">
                    <div className="stat-content">
                        <div className="stat-value">{schoolHolidaysInRange.length}</div>
                        <div className="stat-label">Regionen mit Ferien</div>
                    </div>
                </div>
                <div className="stat-card stat-affected">
                    <div className="stat-content">
                        <div className="stat-value">{formatPopulation(totalAffectedPop)}</div>
                        <div className="stat-label">Betroffene Bevölkerung</div>
                        <div className="stat-percentage">{percentageAffected}% von {getCountryName(mainCountry.code)}</div>
                    </div>
                </div>
            </div>

            {/* FEIERTAGE SECTION - GRUPPIERT */}
            {holidaysInRange.length > 0 && (
                <div className="holidays-section">
                    <h4 className="section-title">Feiertage</h4>
                    <div className="holidays-list-compact">
                        {groupedHolidaysCompact.map((group, index) => {
                            const totalPopulation = group.regions.reduce((sum, r) => sum + r.population, 0);

                            return (
                                <div key={index} className="holiday-group-compact">
                                    <div className="holiday-header-compact">
                                        <div className="holiday-title">
                                            <span className="holiday-name-bold">{group.name}</span>
                                            <span className="holiday-dates-compact">
                                                {formatDateShort(group.date)}
                                            </span>
                                        </div>
                                        <div className="total-population-badge holiday-badge">
                                            {formatPopulation(totalPopulation)} gesamt
                                        </div>
                                    </div>
                                    <div className="regions-grid-compact">
                                        {group.regions.map((region) => (
                                            <div key={region.code} className="region-item-compact">
                                                <span className="country-flag-tiny">{region.countryCode}</span>
                                                <span className="region-name-tiny">
                                                    {region.isGlobal ? `${region.name} (landesweit)` : region.name}
                                                </span>
                                                <span className="region-population-tiny">{formatPopulation(region.population)}</span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            )}

            {/* SCHULFERIEN SECTION - GRUPPIERT */}
            {groupedSchoolHolidays.length > 0 && (
                <div className="school-holidays-section">
                    <h4 className="section-title">Schulferien</h4>
                    <div className="school-holidays-list-compact">
                        {groupedSchoolHolidays.map((group, index) => {
                            const totalPopulation = group.regions.reduce((sum, r) => sum + r.population, 0);

                            return (
                                <div key={index} className="school-holiday-group-compact">
                                    <div className="school-holiday-header-compact">
                                        <div className="school-holiday-title">
                                            <span className="holiday-name-bold">{group.name}</span>
                                            <span className="holiday-dates-compact">
                                                {formatDateShort(group.startDate)} - {formatDateShort(group.endDate)}
                                            </span>
                                        </div>
                                        <div className="total-population-badge">
                                            {formatPopulation(totalPopulation)} gesamt
                                        </div>
                                    </div>
                                    <div className="regions-grid-compact">
                                        {group.regions.map((region) => (
                                            <div key={region.code} className="region-item-compact">
                                                <span className="country-flag-tiny">{region.countryCode}</span>
                                                <span className="region-name-tiny">{region.name}</span>
                                                <span className="region-population-tiny">{formatPopulation(region.population)}</span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            )}
        </div>
    );
};

export default SelectedRangeInfo;