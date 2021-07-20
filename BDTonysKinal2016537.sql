Create database BDTonysKinal2016537;
use BDTonysKinal2016537;
#drop database BDTonysKinal2016537;
create table TipoEmpleado 
(codigoTipoEmpleado int not null auto_increment primary key,
descripcion varchar(100) not null);

create table Empleados
(codigoEmpleado int not null auto_increment primary key,
numeroEmpleado int not null,
apellidosEmpleado varchar(150) not null,
nombresEmpleado varchar(150) not null,
direccionEmpleado varchar(150) not null,
telefonoContacto varchar(10) not null,
gradoCocinero varchar(50) ,
codigoTipoEmpleado int not null,
constraint FK_CodigoTipoPlato foreign key(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado));

create table Empresas 
(codigoEmpresa int not null auto_increment primary key,
nombreEmpresa varchar(150) not null,
direccion varchar(150) not null,
telefono varchar(10) not null);

create table Presupuesto 
(codigoPresupuesto int not null auto_increment primary key,
fechaSolicitud date not null,
cantidadPresupuesto decimal(10,2) not null,
codigoEmpresa int not null,
constraint FK_CodigoEmpresa foreign key(codigoEmpresa) references Empresas(codigoEmpresa));

create table Servicios 
(codigoServicio int not null auto_increment primary key,
fechaServicio date not null,
tipoServicio varchar(100) not null,
horaServicio time not null,
lugarServicio varchar(100) not null,
telefonoContacto varchar(10) not null,
codigoEmpresa int,
constraint FK_CodigoEmpresas foreign key(codigoEmpresa) references Empresas(codigoEmpresa));

create table Productos 
(codigoProducto int not null auto_increment primary key,
nombreProducto varchar(150) not null,
cantidad int not null);

create table TipoPlato 
(codigoTipoPlato int not null auto_increment primary key,
descripcionTipo varchar(100) not null);

create table Platos 
(codigoPlato int not null auto_increment primary key,
cantidad int not null,
nombrePlato varchar(50) not null,
descripcionPlato varchar(150) not null,
precioPlato decimal(10,2) not null,
codigoTipoPlato int not null,
constraint FK_CodigoTipoPlatos foreign key(codigoTipoPlato) references TipoPlato(codigoTipoPlato));

create table productos_has_Platos 
(Productos_codigoProducto int not null,
codigoProducto int not null,
codigoPlato int not null,
primary key  PK_Productos_codigoProducto (Productos_codigoProducto),
constraint FK_CodigoProducto foreign key(codigoProducto) references Productos(codigoProducto),
constraint FK_CodigoPlato foreign key(codigoPlato) references Platos(codigoPlato));

create table Servicios_has_Platos 
(Servicios_codigoServicio int not null,
codigoServicio int not null,
codigoPlato int not null,
primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
constraint FK_CodigoServicio foreign key(codigoServicio) references Servicios(codigoServicio),
constraint FK_CodigoPlatos foreign key(codigoPlato) references Platos(codigoPlato));

create table Servicios_has_Empleados 
(Servicios_codigoServicio int not null auto_increment,
codigoServicio int not null,
codigoEmpleado int not null,
fechaEvento date not null,
horaEvento time not null,
lugarEvento varchar(150) not null,
primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
constraint FK_CodigoServicios foreign key(codigoServicio) references Servicios(codigoServicio),
constraint FK_CodigoEmpleado foreign key(codigoEmpleado) references Empleados(codigoEmpleado));

/*=================TipoEmpleado================== */
#==================Listar========================
delimiter $$
	create procedure sp_ListarTipoEmpleado()
		begin
			select codigoTipoEmpleado, 
			descripcion from tipoEmpleado;
		end $$
delimiter ;
#==============Agregar===================
delimiter $$
	create procedure sp_AgregarTipoEmpleado
	(in descripcionI varchar(100))
		begin
			insert into TipoEmpleado(descripcion) 
			values(descripcionI);
		end $$
delimiter ;
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Organizador');
#==============Actualizar================
delimiter $$
	create procedure sp_ActualizarTipoEmpleado
	(in idI int,
	in descripcionI varchar(100))
		begin
			update TipoEmpleado set 
			descripcion=descripcionI 
		where codigoTipoEmpleado=idI;
	end $$
delimiter ;
#=============Eliminar==================
delimiter $$
	create procedure sp_EliminarTipoEmpleado
	(in idI int)
		begin
			delete from Empleados
            where tipoEmpleado_codigoTipoEmpleado;
			delete from TipoEmpleado 
			where codigoTipoEmpleado=idI;
	end $$
delimiter ;
#=============Buscar====================
delimiter $$
	create procedure sp_BuscarTipoEmpleado
	(in idI int)
		begin
			select codigoTipoEmpleado,
			descripcion 
			from TipoEmpleado 
			where codigoTipoEmpleado=idI;
		end $$
delimiter ;

/*===================Empleados================*/
#===============Listar====================
delimiter $$
	create procedure sp_ListarEmpleados()
		begin
			select codigoEmpleado, 
			numeroEmpleado, 
			apellidosEmpleado, 
			nombresEmpleado,
			direccionEmpleado, 
			telefonoContacto,
			gradoCocinero, 
			codigoTipoEmpleado  
		from Empleados;
	end $$
delimiter ;
#===============Agregar===================
delimiter $$
	create procedure sp_AgregarEmpleados
	(in numeroEmpleadoI int,
	in apellidosEmpleadoI varchar(150),
	in nombresEmpleadoI varchar(150),
	in direccionEmpleadoI varchar(150),
	in telefonoContactoI varchar(10),
	in gradoCocineroI varchar(50),
	in codigoTipoEmpleadoI int)
		begin
			insert into Empleados
			(numeroEmpleado,
			apellidosEmpleado,
			nombresEmpleado,
			direccionEmpleado,
			telefonoContacto,
			gradoCocinero,
			codigoTipoEmpleado)
			values (numeroEmpleadoI,
			apellidosEmpleadoI,
			nombresEmpleadoI,
			direccionEmpleadoI,
			telefonoContactoI,
			gradoCocineroI,
			codigoTipoEmpleadoI);
		end $$
delimiter ;
call sp_AgregarEmpleados(2,'Martinez Gonzales','Juan Carlos','Zona 1','56005864','1',1);
call sp_AgregarEmpleados(4,'Perez Hernandes','Jose David','Zona 9','76542398','3',2);
call sp_AgregarEmpleados(3,'Rivera Estrada','Carla Elizabet','Zona 11','87564323','2',3);
call sp_AgregarEmpleados(5,'Leal Godines','Jonatan Elias','Zona 18','45367643','4',4);
call sp_AgregarEmpleados(1,'Gutierrez De Leon','Selena Esmeralda','Zona 2','42358769','5',5);
#===============Actualizar==============
delimiter $$
	create procedure sp_ActualizarEmpleados
	(in idI int,
	in numeroEmpleadoI int,
	in apellidosEmpleadoI varchar(150),
	in nombresEmpleadoI varchar(150),
	in direccionEmpleadoI varchar(150),
	in telefonoContactoI varchar(10),
	in gradoCocineroI varchar(50))
		begin
			update Empleados set 
			numeroEmpleado=numeroEmpleadoI,
			apellidosEmpleado=apellidosEmpleadoI,
			nombresEmpleado=nombresEmpleadoI,
			direccionEmpleado=direccionEmpleadoI,
			telefonoContacto=telefonoContactoI,
			gradoCocinero=gradoCocineroI
			where codigoEmpleado=idI;
		end $$
delimiter ;
#==============Eliminar==============
delimiter $$
	create procedure sp_EliminarEmpleados
	(in idI int)
		begin
			delete from Empleados where codigoEmpleado=idI;
		end $$
delimiter ;
#==============Buscar================
delimiter $$
	create procedure sp_BuscarEmpleados
	(in idI int)
		begin
			select codigoEmpleado, 
			numeroEmpleado, 
			apellidoEmpleado, 
			nombresEmpleado,
			direccionEmpleado, 
			telefonoContacto,
			gradoCocinero, 
			codigoTipoEmpleado 
			from Empleados 
			where codigoEmpleado=idI;
		end $$
delimiter ;

/*===============Empresas===================*/
#================Listar==============
delimiter $$
	create procedure sp_ListarEmpresas()
		begin
			select codigoEmpresa, 
			nombreEmpresa,
			direccion, 
			telefono 
			from Empresas;
		end $$
delimiter ;
#================Agregar=============
delimiter $$
	create procedure sp_AgregarEmpresas
	(in nombreEmpresa varchar(150),
	in direccion varchar(150),
	telefono varchar(10))
		begin
			insert into Empresas
			(nombreEmpresa,
			direccion,
			telefono) 
			values(nombreEmpresa,
			direccion,
			telefono);
		end $$
delimiter ;
call sp_AgregarEmpresas('MetalEnvaces','zona 3 de Mixco','54432651');
call sp_AgregarEmpresas('Cementos Progreso','zona 11 de Villa Nueva','43653898');
call sp_AgregarEmpresas('Caca Cola','zona 14','56874352');
call sp_AgregarEmpresas('Tigo','zona 12 Villa Lobos','44362375');
call sp_AgregarEmpresas('& Cafe','zona 11 la Reformita','83743209');
#===============Actualizar===========
delimiter $$
	create procedure sp_ActualizarEmpresas
	(in idI int,
	in nombreEmpresaI varchar(150),
	in direccionI varchar(150),
	telefonoI varchar(10))
		begin
			update Empresas set 
			nombreEmpresa=nombreEmpresaI,
			direccion=direccionI,
			telefono=telefonoI
			where codigoEmpresa=idI;
		end $$
delimiter ;
#===============Eliminar============
delimiter $$
	create procedure sp_EliminarEmpresas
	(in idI int)
		begin
			delete from presupuesto
            where Empresas_codigoEmpresa=idI;
            delete from servicios
            where Empresas_codigoEmpresa=idI;
			delete from Empresas 
			where codigoEmpresa=idI;
		end $$
delimiter ;
#===============Buscar=============
delimiter $$
	create procedure sp_BuscarEmpresas
	(in idI int)
		begin
			select codigoEmpresa,
			nombreEmpresa,
			direccion, 
			telefono 
			from Empresas 
			where codigoEmpresa=idI;
		end $$
delimiter ;

/*=====================Presupuesto===================*/
#===============Listar==============
delimiter $$
	create procedure sp_ListarPresupuesto()
		begin
			select codigoPresupuesto, 
			fechaSolicitud,
			cantidadPresupuesto, 
			codigoEmpresa 
			from Presupuesto;
		end $$
delimiter ;
#===============Agregar=============
delimiter $$
	create procedure sp_AgregarPresupuesto
	(in fechaSolicitudI date,
	in cantidadPresupuestoI decimal(10,2),
	in codigoEmpresaI int)
		begin 
			insert into Presupuesto
			(fechaSolicitud,
			cantidadPresupuesto,
			codigoEmpresa)
			values(fechaSolicitudI,
			cantidadPresupuestoI,
			codigoEmpresaI);
		end $$
delimiter ;
call sp_AgregarPresupuesto('2020-07-02',18000,1);
call sp_AgregarPresupuesto('2020-07-09',9000,2);
call sp_AgregarPresupuesto('2020-07-15',15000,3);
call sp_AgregarPresupuesto('2020-07-23',21000,4);
call sp_AgregarPresupuesto('2020-07-29',25000,5);

call sp_AgregarPresupuesto('2020-07-26',22000,1);
call sp_AgregarPresupuesto('2020-07-15',11500,2);
#=============Actualizar==============
delimiter $$
	create procedure sp_ActualizarPresupuesto
	(in idI int,
	in fechaSolicitudI date,
	in cantidadPresupuestoI decimal(10,2))
		begin
			update Presupuesto set 
			fechaSolicitud=fechaSolicitudI,
			cantidadPresupuesto=cantidadPresupuestoI
			where codigoPresupuesto=idI;
		end $$
delimiter ;
#============Eliminar=====================
delimiter $$
	create procedure sp_EliminarPresupuesto
	(in idI int)
		begin
			delete from Presupuesto where 
			codigoPresupuesto=idI;
		end $$
delimiter ;
#============Buscar=====================
delimiter $$
	create procedure sp_BuscarPresupuesto
	(in idI int)
		begin
			select codigoPresupuesto, 
			fechaSolicitud,
			cantidadPresupuesto, 
			codigoEmpresas 
			from Presupuesto 
			where codigoPresupuesto=idI;
		end $$
delimiter ;

/*=================Servicios==============*/
#=============Listar==========================
delimiter $$
	create procedure sp_ListarServicios()
		begin
			select codigoServicio, 
			fechaServicio,
			tipoServicio, 
			horaServicio,
			lugarServicio, 
			telefonoContacto,
			codigoEmpresa 
			from Servicios;
		end $$
delimiter ;
#=============Agregar=========================
delimiter $$
	create procedure sp_AgregarServicios
	(in fechaServicioI date,
	in tipoServicioI varchar(100),
	in horaServicioI time,
	in lugarServicioI varchar(100),
	in telefonoContactoI varchar(10),
	in codigoEmpresaI int)
		begin
			insert into Servicios(fechaServicio,
			tipoServicio,
			horaServicio,
			lugarServicio,
			telefonoContacto,
			codigoEmpresa)
			values (fechaServicioI,
			tipoServicioI,
			horaServicioI,
			lugarServicioI,
			telefonoContactoI,
			codigoEmpresaI);
		end $$
delimiter ;
call sp_AgregarServicios('2020-07-31','Aniversario','14:30:00','zona 10, La Cabaña','45678745',1);
call sp_AgregarServicios('2020-08-06','Convivio','13:00:00','zona 1, El Rosal','44763456',2);
call sp_AgregarServicios('2020-08-17','Fiesta trimestral','17:00:00','zona 18, San Vicente ','56785423',3);
call sp_AgregarServicios('2020-08-29','Aniversario','15:15:00','zona 11, Rumbar','67345254',4);
call sp_AgregarServicios('2020-09-03','Convivio','12:45:00','zona 11, Cañas','59453241',5);

call sp_AgregarServicios('2020-12-25','Fiesta de Navidad','19:00:00','Zona 1, El Auditorio',56006854,1);
call sp_AgregarServicios('2020-09-15','Celebracion del día de la Indipendencia','09:30:00','zona 3',34545643,2);
#==============Actualizar===================
delimiter $$
	create procedure sp_ActualizarServicios
	(in idI int, 
	in fechaServicioI date,
	in tipoServicioI varchar(100),
	in horaServicioI time,
	in lugarServicioI varchar(100),
	in telefonoContactoI varchar(10))
		begin
			update Servicios set 
			fechaServicio=fechaServicioI,
			tipoServicio=tipoServicioI,
			horaServicio=horaServicioI,
			lugarServicio=lugarServicioI,
			telefonoContacto=telefonoContactoI
			where codigoServicio=idI;
		end $$
delimiter ;
#================Eliminar==============
delimiter $$
	create procedure sp_EliminarServicios
	(in idI int)
		begin 
			delete from Servicios where 
			codigoServicios=idI;
		end $$
delimiter ;
#================Buscar===============
delimiter $$
	create procedure sp_BuscarServicios
	(in idI int)
		begin
			select codigoServicio, 
			fechaServicio,
			tipoServicio, 
			horaServicio,
			lugarServicio, 
			telefonoContacto,
			codigoEmpresa 
			from Servicios 
			where codigoServicio=idI;
		end $$
delimiter ;

/*===============Productos==================*/
#=============Listar================
delimiter $$
	create procedure sp_ListarProductos()
		begin
			select codigoProducto, 
			nombreProducto,
			cantidad 
			from Productos;
		end $$
delimiter ;
#=============Agregar===============
delimiter $$
	create procedure sp_AgregarProductos
	(in nombreProductoI varchar(150),
	in cantidadI int)
		begin
			insert into Productos
			(nombreProducto,
			cantidad)
			values
			(nombreProductoI,
			cantidadI);
		end $$
delimiter ;
call sp_AgregarProductos('Carne',29);
call sp_AgregarProductos('Cerveza',15);
call sp_AgregarProductos('Cebollitas',70);
call sp_AgregarProductos('cupcakes',50);
call sp_AgregarProductos('Galgerias',34);

call sp_AgregarProductos('Tomates',25);
call sp_AgregarProductos('Aguacates',35);
call sp_AgregarProductos('Arroz',33);
call sp_AgregarProductos('Frijol',28);
#============Actualizar=============
delimiter $$
	create procedure sp_ActualizarProductos
	(in idI int,
	in nombreProductoI varchar(150),
	in cantidadI int)
		begin
			update Productos set 
			nombreProducto=nombreProductoI,
			cantidad=cantidadI
			where codigoProducto=idI;
		end $$
delimiter ;
#==========Eliminar=================
delimiter $$
	create procedure sp_EliminarProductos
	(in idI int)
		begin
			delete from Productos 
			where codigoProducto=idI;
		end $$
delimiter ;
#==========Buscar===================
delimiter $$
	create procedure sp_BuscarProductos
	(in idI int)
		begin
			select codigoProducto, 
			nombreProducto,
			cantidad 
			from Productos 
			where codigoProducto=idI;
		end $$
delimiter ;

/*=================TipoPlato========================*/
#=================Listar==============
delimiter $$
	create procedure sp_ListarTipoPlato()
		begin
			select codigoTipoPlato,
			descripcionTipo 
			from TipoPlato;
		end $$
delimiter ;
#=================Agregar=============
delimiter $$
	create procedure sp_AgregarTipoPlato
	(in descripcionTipoI varchar(100))
		begin
			insert into TipoPlato(descripcionTipo) 
			values(descripcionTipoI);
		end $$
delimiter ;
call sp_AgregarTipoPlato('Churrasco');
call sp_AgregarTipoPlato('Hamburgesas');
call sp_AgregarTipoPlato('Shucos');
call sp_AgregarTipoPlato('Comida Tipica');
call sp_AgregarTipoPlato('Caldo de Res');
#=================Actualizar===========
delimiter $$
	create procedure sp_ActualizarTipoPlato
	(in idI int,
	in descripcionTipoI varchar(100))
		begin
			update TipoPlato set 
			descripcionTipo=descripcionTipoI 
			where codigoTipoPlato=idI;
		end $$
delimiter ;
#================Eliminar==============
delimiter $$
	create procedure sp_EliminarTipoPlato
	(in idI int)
		begin
			delete from platos
            where TipoPlato_codigoTipoPlato=idI;
			delete from TipoPlato 
			where codigoTipoPlato=idI;
		end $$
delimiter ;
#================Buscar===============
delimiter $$
	create procedure sp_BuscarTipoPlato
	(in idI int)
		begin
			select codigoTipoPlato,
			descripcionTipo 
			from TipoPlato 
			where codigoTipoPlato=idI;
		end $$
delimiter ;

/*================Platos=================*/
#=================Listar====================
delimiter $$
	create procedure sp_ListarPlatos()
		begin
			select codigoPlato, 
			cantidad,
			nombrePlato, 
			descripcionPlato,
			precioPlato, 
			codigoTipoPlato 
			from Platos;
		end $$
delimiter ;
#=================Agregar===================
delimiter $$
	create procedure sp_AgregarPlatos
	(in cantidadI int,
	in nombrePlatoI varchar(50),
	in descripcionPlatoI varchar(150),
	in precioPlatoI decimal(10,2),
	in codigoTipoPlatoI int)
		begin
			insert into Platos(cantidad,
			nombrePlato,
			descripcionPlato,
			precioPlato,
			codigoTipoPlato)
			values(cantidadI,
			nombrePlatoI,
			descripcionPlatoI,
			precioPlatoI,
			codigoTipoPlatoI);
		end $$
delimiter ;
call sp_AgregarPlatos(45,'Carnita asada','Churrasco familiar',42,1);
call sp_AgregarPlatos(55,'CangreBurger','La tapa arterias',40,2);
call sp_AgregarPlatos(49,'Shuco especial','Para chuparse los dedos',28,3);
call sp_AgregarPlatos(70,'Tipica','Para sentir el sabor chapin',30,4);
call sp_AgregarPlatos(66,'Sopa','Como lo hacia mamá',38,5);
#=================Actualizar===============
delimiter $$
	create procedure sp_ActualizarPlatos
	(in idI int,
	in cantidadI int,
	in nombrePlatoI varchar(50),
	in descripcionPlatoI varchar(150),
	in precioPlatoI decimal(10,2))
		begin
			update Platos set 
			cantodad=cantidadI,
			nombrePlato=nombrePlatoI,
			descripcionPlato=descripcionPlatoI,
			precioPlato=precioPlatoI
			where codigoPlato=idI;
		end $$
delimiter ;
#==============Eliminar==============
delimiter $$
	create procedure sp_EliminarPlatos
	(in idI int)
		begin
			delete from Platos where 
			codigoPlato=idI;
		end $$
delimiter ;
#=============Buscar=================
delimiter $$
	create procedure sp_BuscarPlatos
	(in idI int)
		begin
			select codigoPlato, 
			cantidad,
			nombrePlato, 
			descripcionPlato,
			precioPlato, 
			codigoTipoPlato 
			from Platos where 
			codigoPlato=idI;
		end $$
delimiter ;
#=============================================================

/*====================Productos_has_Platos=======================*/
#===================Listar=================
delimiter $$
	create procedure sp_ListarProductos_has_Platos()
		begin
			select pr.codigoProducto, p.codigoPlato
            from productos pr  inner join platos p on
			pr.codigoProducto=p.codigoPlato;
		end $$
delimiter ;

/*========================Servicios_has_Platos======================*/
#================Listar================
delimiter $$
	create procedure sp_ListarServicios_has_Platos()
		begin
			select s.codigoServicio, p.codigoPlato
            from servicios s inner join platos p on
			s.codigoServicio=p.codigoPlato;
		end $$
delimiter ;


/*======================Servicios_has_Empleados==================*/
#===============Listar================
delimiter $$
	create procedure sp_ListarServicios_has_Empleados()
		begin
			select Servicios_codigoServicio,
            codigoServicio, 
			codigoEmpleado,
			fechaEvento, 
			horaEvento, 
			lugarEvento from 
			servicios_has_empleados;
		end $$
delimiter ;
#===============Agregar===============
delimiter $$
	create procedure sp_AgregarServicios_has_Empleados
	(in codigoServicioI int,
	in codigoEmpleadoI int,
	in fechaEventoI date,
	in horaEventoI time,
	in lugarEventoI varchar(150))
		begin
			insert into Servicios_has_Empleados
			(codigoServicio,
			codigoEmpleado,
			fechaEvento,
			horaEvento,
			lugarEvento)
			values
			(codigoServicioI,
			codigoEmpleadoI,
			fechaEventoI,
			horaEventoI,
			lugarEventoI);
		end $$
delimiter ;

call sp_AgregarServicios_has_Empleados(1,1,'2020-12-09','18:45:00','zona 3 de mixco, local 32');
call sp_AgregarServicios_has_Empleados(2,2,'2020-11-25','19:45:00','zona 5, la esperanza');
call sp_AgregarServicios_has_Empleados(3,3,'2020-12-18','16:00:00','zona 2 de villa nueva, MyHouse');
call sp_AgregarServicios_has_Empleados(4,4,'2020-12-04','13:15:00','zona 18, La Casa de el amigo');
call sp_AgregarServicios_has_Empleados(5,5,'2021-01-03','15:30:00','zona 1, Parque Central');


#===================Actualizar===============
delimiter $$
	create procedure sp_ActualizarServicios_has_Empleados
	(in Servicios_codigoServicioI int,
	in fechaEventoI date,
	in horaEventoI time,
    in lugarEventoI varchar(150))
		begin
			update Servicios_has_Empleados set 
			fechaEvento=fechaEventoI,
			horaEvento=horaEventoI,
			lugarEvento=lugarEventoI
			where Servicios_codigoServicio=Servicios_codigoServicioI;
		end $$
delimiter ;
#=================Eliminar=================
delimiter $$
	create procedure sp_EliminarServicios_has_Empleados
    (in Servicios_codigoServicioI int)
		begin
			delete from servicios_has_empleados
            where Servicios_codigoServicio=Servicios_codigoServicioI ;
        end $$
delimiter ;
#=================Buscar===================
delimiter $$
	create procedure sp_BuscarServicios_has_Empleados
    (in Servicios_codigoServicioI int)
		begin
			select Servicios_codigoServicio,
			Empleados_codigoEmpleado,
			fechaEvento,
			horaEvento,
			lugarEvento
			from Servicios_has_Empleados
			where Servicios_codigoServicio=Servicios_codigoServicioI ;
		end $$
delimiter ;
#=================Reporte Presupuesto=======
delimiter $$
	create procedure sp_ReportePresupuesto(in codEmpresa int)
		begin
			select e.nombreEmpresa, e.direccion, e.telefono, s.fechaServicio, s.horaServicio, s.lugarServicio 
            from empresas e inner join servicios s on
			e.codigoEmpresa = s.codigoEmpresa 
				where e.codigoEmpresa = codEmpresa;
		end $$
delimiter ;

#====================SubReporte de Presupuesto=========
delimiter $$
	create procedure sp_SubReportePresupuesto(in codEmpresa int)
		begin
			select p.fechaSolicitud, p.cantidadPresupuesto from empresas e inner join presupuesto p on
			e.codigoEmpresa = p.codigoEmpresa 
				where e.codigoEmpresa = codEmpresa group by p.cantidadPresupuesto;
		end $$
delimiter ;
#====================Reporte Servicio===============
delimiter $$
	create procedure sp_ReporteServicio(in codServicio int)
		begin
			select p.cantidad, tp.descripcionTipo from servicios s inner join platos p on
            s.codigoServicio=p.codigoPlato
				inner join tipoPlato tp on
                p.codigoTipoPlato=tp.codigoTipoPlato
					where s.codigoServicio=codServicio ;
		end $$
delimiter ;
#====================SubReporte Servicio=============
delimiter $$
	create procedure sp_SubReporteServicio()
		begin
			select pr.nombreProducto from platos p inner join productos pr 
            group by pr.nombreProducto;
        end $$
delimiter ;
