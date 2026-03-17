import React from "react";

const StatCard = ({ title, value, icon: Icon, color }) => {
  return (
    <div className="col-xl-3 col-md-6 mb-4">
      <div className="stat-card">

        <div className={`icon text-${color}`}>
          <Icon size={28} />
        </div>

        <div>
          <p className="text-muted small">{title}</p>
          <h4>{value}</h4>
        </div>

      </div>
    </div>
  );
};

export default StatCard;