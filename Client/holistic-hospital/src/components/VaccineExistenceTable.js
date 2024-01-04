import React, { useState, useEffect, useContext, useRef } from "react";
import axios from 'axios';
import { UserContext } from "../contexts/UserContext/UserContext";
import MenuContext from "../contexts/MenuContext/MenuContext";

//Components imports
import { DataTable } from "primereact/datatable";
import { Column } from 'primereact/column';
import { Toolbar } from 'primereact/toolbar';
import { Button } from "primereact/button";
import './cssFiles/DataTable.css';



import DeleteOneVaccine from "./EmergentWindows/DeleteVaccineExistence";

import CreateNewVaccine from "./EmergentWindows/CreateNewVaccine";
import EditVaccineExistence from "./EmergentWindows/EditVaccineExistence";

export default function VaccineExistenceTable() {
  const menuContext = useContext(MenuContext);
  const { token } = useContext(UserContext);

  const [codevar, setcodevar] = useState("");
  const [namevar, setnamevar] = useState("");

  const dt = useRef(null);
  const [loading, setLoading] = useState(true);
  const [vaccinesList, setVaccinesList] = useState([]);

  useEffect(() => {
    try {
      axios.get(process.env.REACT_APP_API_URL + "admin/vaccines", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => {
          if (res.status === 200) {
            setVaccinesList(res.data);
            setLoading(false);
          }
        }).catch(err => console.error(err));
    } catch (error) {
      throw console.error(error);
    }
  })

  const leftToolbarTemplate = () => {
    return (
      <>
        <Button
          label="Nuevo"
          icon="pi pi-plus"
          className="p-button-success mr-2"
          onClick={() => menuContext.settingEmergentNewVaccineState()}
        />
      </>
    )
  }


  const actionBodyTemplate = (rowData) => {
    return (
      <>
        <Button
          icon="pi pi-pencil"
          tooltip="Editar"
          tooltipOptions={{ position: 'bottom' }}
          className="p-button-rounded p-button-success mr-2"
          onClick={() => {
            setcodevar(rowData.id_vaccine);
            setnamevar(rowData.name);
            menuContext.settingEmergentEditVaccineState();
          }}
        />
        <Button
          icon="pi pi-trash"
          tooltip="Eliminar"
          tooltipOptions={{ position: 'bottom' }}
          className="p-button-rounded p-button-warning"
          onClick={() => {
            setnamevar(rowData.name);
            setcodevar(rowData.id_vaccine);
            menuContext.settingEmergentDeleteOneVaccineState();
          }}
        />
      </>
    );
  }

  const header = (
    <div className="table-header">
      <h5 className="mx-0 my-1">Manejo de vacunas</h5>
    </div>
  );

  return (
    <div className="w-full overflow-hidden">
      {/*
        *User creation emergent window 
      */}
      <CreateNewVaccine />

      {/*
          *User edit emergent window 
        */}
      <EditVaccineExistence code={codevar} name={namevar} />

      {/*
          *User deletion emergent window 
        */}
      <DeleteOneVaccine code={codevar} name={namevar} />

      <div className="card">

        <Toolbar className="mb-4" left={leftToolbarTemplate} ></Toolbar>

        <DataTable showGridlines lazy={true} ref={dt} value={vaccinesList}
          dataKey="id" paginator rows={10} rowsPerPageOptions={[5, 10, 25]} totalRecords={vaccinesList.length}
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          currentPageReportTemplate="Mostrando {first} - {last} de {totalRecords} vacunas"
          loading={loading} header={header} responsiveLayout="scroll"
        >
          <Column field="name" header="Nombre" style={{ minWidth: '20rem' }}></Column>
          <Column field="required_doses" header="Dosis requeridas" style={{ minWidth: '20rem' }}></Column>
          <Column body={actionBodyTemplate} exportable={false} style={{ minWidth: '10rem' }}></Column>
        </DataTable>
      </div>
    </div>
  )
}