El proyecto browserMongo:
----------------------------
El proyecto "browserMongo" se trata de una aplicacion multi-idioma cuya finalidad es la navegacion y gestion de el sistema de directorios de Windows.
La compatibilidad con otros sistemas operativos mientras que es posible no ha sido probada.

Como ejecutar el programa:
-----------------------------
Para ejecutar el programa es necesario disponer de Eclipse, una vez con el mismo abriremos el proyecto a traves de este
para despues darle a ejecutar el proyecto.

Requisitos necesarios:
-------------------------
Los requisitos minimos para ejecutar este programa es tener un servidor mongo en marcha con las colecciones "error", "literal" y "log",
sin estos requisitos el programa no funcionara, en primer lugar debido a que no podra encontrar dichas colecciones de donde extraer el texto
del programa.

Configuracion Mongo:
-----------------------
La configuracion de mongo se realiza a traves de la clase MongoUtil, dentro de la misma se puede encontrar en el constructor de la clase la ip
del servidor mongo y en la linea de abajo el nombre de la base de datos.

Como usar la aplicacion:
---------------------------
El uso de la aplicacion es simple, en primer lugar debemos ejecutar el programa, tras lo cual se nos dara la ruta del directorio en el que nos encontremos,
seguidamente debemos introducir un comando en pantalla, en caso de no saber que comandos hay se puede usar el comando "help", usar el comando "help" y poner
como parametro el nombre de otro comando dara una breve descripcion del mismo. La aplicacion finalizara despues de introducir "exit" a traves del programa o 
al cerrarlo de forma forzosa.

Datos a tener en cuenta:
---------------------------
Por defecto se guardaran logs de avisos, errores y comandos, en caso de querer cambiarlo debes dirijirte a la clase MongoUtil y cambiar las variables 
"_'nombre'Activated" a voluntad, en caso de querer introducir nuevos logs puedes usar la funcion "createLog()" de MongoUtil para ello, teniendo que introducir 
solamente al llamarla el tipo de log del cual se trata y la descripcion que tendra el mismo.

Listado de metodos:
----------------------
Browser.java
	> init(): Este metodo inicia el navegador, primero examina si el comando es "script" o "exit", en caso contrario ejecutara Browser.selectCommand() para examinar el resto de posibilidades.
	> uploadLog(TreatEntry, LogType): Un pequeño metodo que recoge el nombre del comando usado, sus parametros y crea un log con ellos a traves de la ejecucion de MongoUtil.createLog().
	> selectCommand(TreatEntry): Encuentra el tipo de entrada y ejecuta el metodo apropiado para el mismo.
	> commandGOTO(String): Un metodo usado para viajar entre directorios.
	> commandGOLAST(): Este metodo sirve para viajar al ultimo directorio en el que estuvo.
	> commandLIST(): Lista todo los archivos y carpetas dentro de un directorio.
	> commandUp(): Intenta ir al directorio padre de el directorio actual.
	> commandINFOFILE(String): Muestra informacion de el archivo referenciado.
	> commandINFODIR(String): Muestra informacion de el directorio referenciado.
	> commandHELP(): Un metodo para mostrar todos los comandos disponibles del programa.
	> commandHELP(String): Un metodo para mostrar una breve descripcion del commando referenciado.
	> commandCREATEDIR(String[]): Intentara crear los directorios dados como parametros.
	> commandCREATEFILE(String[]): Intentara crear los archivos dados como parametros.
	> commandDELETEDIR(String[]): Borra los directorios marcados y todo el contenido de los mismos.
	> commandDELETEFILE(String[]): Borra todos los archivos referenciados.
	> commandSORTBY(SortType): Ordena los elementos de un directorio de la forma seleccionada.
	> commandCHANGEIDIOM(Idiom): Deja al usuario cambiar el idioma del programa.
	> commandCLEARLOG(): Borra todos los logs guardados en la base de datos.
	> commandSCRIPT(String): Ejecuta un script dado por el usuario.
	> commandEXIT(): Finaliza el programa.
	> commandERROR(ErrorType): Imprime el error causado.

TreatEntry.java
	>loadObject(): Este metodo tratara los parametros dados a la clase, tranformandolor en EntryType, el resto seran guardados en una String[] y comprobara que no haya errores.
	>obtainEntryType(): Devolvera el tipo de comando.
	>obtainCommand(): Devolverla el comando en tipo String y minuscula.
	>obtainParameters(): Devolvera los parametros en String[]
	>obtainSortType(): Devolvera el tipo de ordenacion.
	>obtainIdiom(): Devolvera el idioma en Idioma.
	>obtainError(): Devolvera el ultimo error generado (en ErrorType).

ErrorControl.java
	>getErrorText(ErrorType): Devolvera una String segun el tipo de error dado.
	>printError(ErrorType): Imprimira el error en consola.

MongoUtil.java
	>changeIdiom(Idiom): Cambiara el idioma para las busquedas en la base de datos.
	>getLiteral(String): Devolvera el texto de la coleccion "literal".
	>getError(String): Devolvera el texto de la coleccion "error".
	>createLog(LogType, String): Un metodo para crear y guardar logs en la coleccion "log".
	>dropLog(): Un metodo para borrar todos los logs de dentro de la coleccion "log".
	>shutdown(): Un metodo para cerrar la conecion con la base de datos de Mongo.
