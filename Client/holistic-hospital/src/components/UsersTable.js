import React, { useState, useEffect, useContext, useRef } from "react";
import axios from "axios";
import MenuContext from "../contexts/MenuContext/MenuContext";
import { UserContext } from "../contexts/UserContext/UserContext";

//Components imports
import { DataTable } from "primereact/datatable";
import { Column } from 'primereact/column';
import { Toolbar } from 'primereact/toolbar';
import { Button } from "primereact/button";
import './cssFiles/DataTable.css';

import CreateNewUser from "./EmergentWindows/CreateNewUser";
import EditUser from "./EmergentWindows/EditUser";
import DeleteOneUser from "./EmergentWindows/DeleteOneUser";

export default function UsersTable() {
  const menuContext = useContext(MenuContext);
  const { token, id_person } = useContext(UserContext);

  const [loading, setLoading] = useState(true);
  const [username, setUsername] = useState("");
  const [id, setId] = useState(null);

  const [usersList, setUsersList] = useState([]);
  const dt = useRef(null);

  useEffect(() => {
    try {
      axios.get(process.env.REACT_APP_API_URL + "admin/users", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => {
          if (res.status === 200) {
            setUsersList(res.data);
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
          onClick={() => menuContext.settingEmergentNewUserState()}
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
          className="p-button-rounded p-button-success mr-2 p-tooltip-bottom"
          onClick={() => {
            setId(rowData.id_person);
            setUsername(rowData.username);
            menuContext.settingEmergentEditUserState();
          }}
        />
        <Button
          icon="pi pi-trash"
          tooltipOptions={{ position: 'bottom' }}
          tooltip="Eliminar"
          className="p-button-rounded p-button-warning"
          onClick={() => {
            setId(rowData.id_person);
            setUsername(rowData.username);
            menuContext.settingEmergentDeleteOneUserState()
          }}
        />
      </>
    );
  }

  const header = (
    <div className="table-header">
      <h5 className="mx-0 my-1">Manejo de usuarios</h5>
    </div>
  );

  const statusBodyTemplate = (rowData) => {
    if (rowData.status === true)
      return <span>Activo</span>
    else
      return <span className="text-red-700">Inactivo</span>
  }

  const nameBodyTemplate = (rowData) => {
    return rowData.name + ' ' + rowData.last_name;
  }

  const genderBodyTemplate = (rowData) => {
    if (rowData.gender === "F") return "Femenino"
    else return "Masculino"
  }

  const filteredPeople = usersList.filter((user) => {
    return id_person !== user.id_person;
  });

  const paginatorLeft = <Button type="button" icon="pi pi-refresh" className="p-button-text" />;
  const paginatorRight = <Button type="button" icon="pi pi-cloud" className="p-button-text" />;

  return (
    <div className="w-full overflow-hidden">
      {/*
        *User creation emergent window 
      */}
      <CreateNewUser />

      {/*
          *User edit emergent window 
        */}
      <EditUser i={id} u={username} />

      {/*
          *User deletion emergent window 
        */}
      <DeleteOneUser i={id} u={username} />

      <div className="card">

        <Toolbar className="mb-4" left={leftToolbarTemplate} ></Toolbar>

        <DataTable showGridlines ref={dt} value={filteredPeople}
          loading={loading} dataKey="id_person" header={header} responsiveLayout="scroll" totalRecords={usersList.length}
          paginator paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
          currentPageReportTemplate="Mostrando {first} - {last} de {totalRecords} usuarios" rows={10} rowsPerPageOptions={[10, 20, 50]}
          paginatorLeft={paginatorLeft} paginatorRight={paginatorRight}
        >
          <Column field="name" header="Nombre" body={nameBodyTemplate} style={{ minWidth: '12rem' }}></Column>
          <Column field="username" header="Nombre de usuaario" style={{ minWidth: '12rem' }}></Column>
          <Column field="gender" body={genderBodyTemplate} header="Género" style={{ minWidth: '8rem' }}></Column>
          <Column field="email" header="Correo" style={{ minWidth: '12rem' }}></Column>
          <Column field="id_role.name" header="Rol" style={{ minWidth: '8rem' }}></Column>
          <Column field="status" header="Estado" body={statusBodyTemplate} sortable style={{ minWidth: '8rem' }}></Column>
          <Column field="birthdate" header="Fecha de nacimiento" style={{ minWidth: '10rem' }}></Column>
          <Column body={actionBodyTemplate} exportable={false} style={{ minWidth: '8rem' }}></Column>
        </DataTable>
      </div>
    </div>
  )
}