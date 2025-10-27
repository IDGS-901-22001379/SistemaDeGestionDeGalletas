# 🍪 **Sistema de Gestión de Galletas POS** — Compras · Producción · Ventas  
**(Java + MySQL + HTML/JS · Microservicios · CQRS · DAO · ViewModel)**

> **Tagline:** Plataforma end-to-end para **compras de insumos**, **producción con existencias/mermas** y **ventas con ticket**, conectada entre **sucursales** vía **microservicios**. Arquitectura por capas (**REST → Controller → CQRS → DAO → MySQL**), **CORS** y **ViewModel** público.

---

## 🚀 Quick look
- ✅ **Flujo completo**: Compras → Producción (lotes/mermas) → Ventas (ticket y cambio)
- 🧠 **Reglas de negocio en CQRS**: validaciones fuertes antes de ir a BD
- 🗄️ **DAO + SP**: detalles de venta enviados en **JSON** a un procedimiento que retorna `id_venta`
- 🌐 **Microservicios multi-sucursal**: `HttpClient` → consolida ventas externas
- 🔒 **CORS** (dev) | 🧩 **ViewModel** para exponer solo lo necesario
- 🧾 **Ticket imprimible** en frontend

---

## 🧩 Módulos principales
- **Compras de insumos**: actualiza existencias (ajustes/mermas)
- **Producción**: transforma insumos en **lotes**; controla **existencias**
- **Ventas**: carrito, validación de stock, **ticket** y **cambio**
- **Mermas**: disminución por **nombre de galleta** y **fecha**
- **Catálogo**: tipos *Unidad*, *Por Kilo*, *Por Cantidad Monetaria*

---

## 🏛️ Arquitectura (capas)
`REST (JAX-RS)` → `Controller` → `CQRS (validaciones)` → `DAO (SP/SQL)` → `MySQL`  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;↘ `AppService (sucursal externa)` → `ViewModel`
---
 ## 🖥️ Frontend (HTML/JS)

- 🧺 **Carrito** con validación de **existencias**
- 💵 **Cobro**: total, pagado y **cambio**
- 🧾 **Ticket imprimible** (HTML mini)
- 📋 **Listado/Detalle** de ventas (click por fila)
- ⚠️ **Mermas** con SweetAlert2

---

## 🌐 Microservicios (multi-sucursal)
**VentaExternaAppService** (Java `HttpClient`) consulta:
El **Controller** puede **combinar** / **exponer directo** el **ViewModel** externo para una **vista federada**.
---

## 🗄️ Base de datos (MySQL)

- SP `insertarVenta` recibe **detalle JSON** y regresa `id_venta`
- Tablas: `ventas`, `detalle_venta`, `galletas` (tipo, costo, existencia)
- Uso de **PreparedStatement** y **cierre seguro** de conexiones

---

## 🧪 Validaciones (resumen)

- **CQRS**: descripción, total, ticket, tipo_venta y cada detalle **> 0**, ids válidos
- **Frontend**: **existencia ≥ cantidad**
- **DAO**: tipos correctos + manejo de conexiones

---

## ⚙️ Puesta en marcha (local)

- **MySQL**: crear BD/tablas; cargar SP `insertarVenta`; configurar credenciales en la clase de conexión.
- **Backend (JAX-RS)**: desplegar en Tomcat/Payara/GlassFish (contexto sugerido: `DON_GALLETO_Ventas`).
- **Frontend**: abrir `index.html` (consume `http://localhost:8080/DON_GALLETO_Ventas/api/...`).
- **CORS**: habilitado en desarrollo (ajustar orígenes en prod).

## 🔌 Endpoints clave
- **POST** `/api/venta/insertar` *(x-www-form-urlencoded)*  
  - `v`: JSON `{ descripcion, total, ticket, tipo_venta }`  
  - `ldv`: JSON `[{ galleta:{id_galleta}, cantidad, subtotal }]`
- **GET** `/api/venta/getAll?activo=true|false`  
  - `true` → ventas locales (ViewModel)  
  - `false` → ventas públicas vía **microservicio** (otra sucursal)

**Ejemplo POST (frontend):**
```js
const venta = { descripcion:"Venta de 3 tipos", total:150, ticket:"T-001", tipo_venta:"Variado" };
const detalles = [
  { galleta:{id_galleta:5}, cantidad:2, subtotal:50 },
  { galleta:{id_galleta:7}, cantidad:1, subtotal:100 }
];

fetch("http://localhost:8080/DON_GALLETO_Ventas/api/venta/insertar", {
  method: "POST",
  headers: { "Content-Type":"application/x-www-form-urlencoded;charset=UTF-8" },
  body: new URLSearchParams({ v: JSON.stringify(venta), ldv: JSON.stringify(detalles) })
});





