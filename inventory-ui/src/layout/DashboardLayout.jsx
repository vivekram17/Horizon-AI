import React from "react";

import Sidebar from "../components/Sidebar";

const DashboardLayout = ({ children }) => {
  return (
    <>
      

      <div className="d-flex">

        {/* Sidebar */}
        <div
          className="bg-light border-end"
          style={{ width: "240px", minHeight: "calc(100vh - 56px)" }}
        >
          <Sidebar />
        </div>

        {/* Page Content */}
        <div className="flex-grow-1 p-4">
          {children}
        </div>

      </div>
    </>
  );
};

export default DashboardLayout;