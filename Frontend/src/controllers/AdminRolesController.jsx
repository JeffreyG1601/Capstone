// src/controllers/AdminRolesController.jsx
import React, { useEffect, useState } from "react";
import axios from "../services/axiosInstance";
import AdminRolesView from "../views/AdminRolesView";

export default function AdminRolesController(){
  const [roles, setRoles] = useState([]);
  const [options, setOptions] = useState([]);
  const [form, setForm] = useState({name:"", description:""});

  useEffect(() => {
    let mounted = true;
    async function init(){
      try{
        const [rRes, oRes] = await Promise.all([
          axios.get("/admin/roles"),
          axios.get("/admin/roles/options")
        ]);
        if(!mounted) return;
        setRoles(rRes.data || []);
        setOptions(oRes.data || []);
      }catch(e){ console.error(e); }
    }
    init();
    return () => { mounted = false; };
  }, []);

  const save = async () => {
    try {
      await axios.post("/admin/roles", form);
      setForm({name:"",description:""});
      const r = await axios.get("/admin/roles");
      setRoles(r.data || []);
    } catch(e){ console.error(e); }
  };

  const del = async (id) => {
    try {
      await axios.delete(`/admin/roles/${id}`);
      setRoles(prev=> prev.filter(p => p.id !== id));
    } catch(e){ console.error(e); }
  };

  return <AdminRolesView roles={roles} options={options} form={form} setForm={setForm} save={save} del={del} />;
}
