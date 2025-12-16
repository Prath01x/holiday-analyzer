import { useState } from 'react';
import { DayAnalysis } from '../types';
import './Calendar.css';

interface Props {
  dayAnalyses: DayAnalysis[];
  startDate: string;
  endDate: string;
  onDateSelect?: (date: string) => void;
  loading?: boolean;
}

const Calendar = ({ dayAnalyses, startDate, endDate, onDateSelect, loading }: Props) => {
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    return { daysInMonth, startingDayOfWeek, year, month };
  };

  const getAnalysisForDate = (date: Date): DayAnalysis | undefined => {
    const dateStr = date.toISOString().split('T')[0];
    return dayAnalyses.find(d => d.date === dateStr);
  };

  const getLoadColor = (level?: DayAnalysis['level']): string => {
    if (!level) return 'transparent';
    switch (level) {
      case 'very_low': return 'var(--color-very-low)';
      case 'low': return 'var(--color-low)';
      case 'medium': return 'var(--color-medium)';
      case 'high': return 'var(--color-high)';
      case 'very_high': return 'var(--color-very-high)';
    }
  };

  const handleDayClick = (day: number) => {
    if (!onDateSelect) return;

    const clickedDate = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day);

    // Vergangene Tage nicht auswählbar
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (clickedDate < today) return;

    const dateStr = clickedDate.toISOString().split('T')[0];
    onDateSelect(dateStr);
  };

  const isDateInRange = (date: Date): boolean => {
    if (!startDate || !endDate) return false;
    const dateStr = date.toISOString().split('T')[0];
    return dateStr >= startDate && dateStr <= endDate;
  };

  const isDateSelected = (date: Date): 'start' | 'end' | 'middle' | null => {
    const dateStr = date.toISOString().split('T')[0];
    if (dateStr === startDate) return 'start';
    if (dateStr === endDate) return 'end';
    if (isDateInRange(date)) return 'middle';
    return null;
  };

  const isPastDate = (date: Date): boolean => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date < today;
  };

  const previousMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  const { daysInMonth, startingDayOfWeek, year, month } = getDaysInMonth(currentMonth);
  const monthName = currentMonth.toLocaleDateString('de-DE', { month: 'long', year: 'numeric' });

  const days = [];

  // Leere Zellen für Tage vor Monatsbeginn (Montag = erster Tag)
  const adjustedStart = startingDayOfWeek === 0 ? 6 : startingDayOfWeek - 1;
  for (let i = 0; i < adjustedStart; i++) {
    days.push(<div key={`empty-${i}`} className="calendar-day empty"></div>);
  }

  // Tage des Monats
  for (let day = 1; day <= daysInMonth; day++) {
    const date = new Date(year, month, day);
    const analysis = getAnalysisForDate(date);
    const isPast = isPastDate(date);
    const selectionType = isDateSelected(date);

    days.push(
        <div
            key={day}
            className={`calendar-day ${isPast ? 'past' : ''} ${selectionType ? `selected-${selectionType}` : ''}`}
            onClick={() => !isPast && handleDayClick(day)}
            style={{
              backgroundColor: analysis ? getLoadColor(analysis.level) : 'transparent',
              cursor: isPast ? 'not-allowed' : 'pointer',
            }}
            title={analysis ? `${analysis.loadPercentage}% Auslastung` : undefined}
        >
          <span className="day-number">{day}</span>
        </div>
    );
  }

  if (loading) {
    return (
        <div className="calendar">
          <div className="calendar-loading">Lade Daten...</div>
        </div>
    );
  }

  return (
      <div className="calendar">
        <div className="calendar-header">
          <button className="calendar-nav" onClick={previousMonth}>
            ‹
          </button>
          <h3 className="calendar-month">{monthName}</h3>
          <button className="calendar-nav" onClick={nextMonth}>
            ›
          </button>
        </div>

        <div className="calendar-weekdays">
          {['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'].map(day => (
              <div key={day} className="calendar-weekday">{day}</div>
          ))}
        </div>

        <div className="calendar-grid">
          {days}
        </div>

        {startDate && endDate && (
            <button className="clear-selection" onClick={() => onDateSelect && onDateSelect('')}>
              Auswahl zurücksetzen
            </button>
        )}
      </div>
  );
};

export default Calendar;