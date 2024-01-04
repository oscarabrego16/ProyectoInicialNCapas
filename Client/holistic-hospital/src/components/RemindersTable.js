import React, { useState, useEffect, useContext, useRef } from "react";
import axios from "axios";
import { UserContext } from '../contexts/UserContext/UserContext';

//Components imports
import { DataTable } from "primereact/datatable";
import { Column } from 'primereact/column';
import './cssFiles/DataTable.css';

export default function RemindersTable() {
  const { token } = useContext(UserContext);
  
  const [loading, setLoading] = useState(true);
  const [remindersList, setRemindersList] = useState([]);
  const dt = useRef(null);

  useEffect(() => {
    try {
      axios.get(process.env.REACT_APP_API_URL + "patient/recordatorios", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => {
          if (res.status === 200) {
            setRemindersList(res.data);
            setLoading(false);
          }
        }).catch(err => console.error(err));
    } catch (error) {
      throw console.error(error);
    }
  })
  
  const header = (
    <div className="table-header">
      <h5 className="mx-0 my-1">Recordatorios</h5>
    </div>
  );

  const detailBodyTemplate = (rowData) => {
    return <span>
        <h1>Tipo: <b>{rowData.id_appointment_type.type_name}</b></h1>
        {
          rowData.id_vaccine !== null ? <h1>Vacuna: <b>{rowData.id_vaccine.name}</b></h1>
          :
          rowData.id_area !== null ? <h1>Área: <b>{rowData.id_area.name}</b></h1>
          :
          rowData.id_test !== null ? <h1>Examen: <b>{rowData.id_test.name}</b></h1>
          :
          ""
        }
    </span>
  }

  return (
    <div className="w-full overflow-hidden">
      <div className="card">
        <DataTable showGridlines lazy={true} ref={dt} value={remindersList}
          dataKey="id" paginator rows={10} rowsPerPageOptions={[5, 10, 25]} totalRecords={remindersList.length}
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          currentPageReportTemplate="Mostrando {first} - {last} de {totalRecords} reminders"
          loading={loading} header={header} responsiveLayout="scroll">
          <Column field="timestamp" header="Fecha del recordatorio" style={{ minWidth: '12rem' }}></Column>
          <Column field="id_appointment_type" header="Detalles del recordatorio" body={detailBodyTemplate} style={{ minWidth: '12rem' }}></Column>
        </DataTable>
      </div>
    </div>
  )
}