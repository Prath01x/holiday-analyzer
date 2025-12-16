import './AdminHeader.css';

interface Props {
    onBack: () => void;
}

const AdminHeader = ({ onBack }: Props) => {
    return (
        <header className="admin-header">
            <div className="admin-header-content">
                <h1>Admin-Bereich</h1>
                <p className="admin-header-subtitle">Verwaltung von Ländern, Regionen, Feiertagen und Ferien</p>
            </div>
            <button className="btn-secondary" onClick={onBack}>
                Zurück zum Dashboard
            </button>
        </header>
    );
};

export default AdminHeader;