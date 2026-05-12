# Todo sobre Java: Conceptos, Historia y Configuración

## 1. ¿Qué es Java?
Java es un lenguaje de programación de **alto nivel** y una plataforma de desarrollo diseñada para ser robusta, segura y portátil.

### Características Principales:
* **Programación Orientada a Objetos (POO):** El código se organiza en torno a datos (objetos) y la lógica que los manipula.
* **Portabilidad:** Su filosofía se resume en el lema: *"Write Once, Run Everywhere"* (Escríbelo una vez, ejecútalo en cualquier lugar).
* **Bytecode y JVM:** A diferencia de otros lenguajes, Java no se compila en código máquina directo. Se compila en un código intermedio llamado **Bytecode**, el cual es interpretado por la **Máquina Virtual de Java (JVM)**. Esto permite que corra en cualquier sistema operativo que tenga instalada la JVM.

---

## 2. Historia y Evolución
Java ha pasado por diversas etapas desde su concepción hasta convertirse en un estándar de la industria:

| Época | Hito Importante | Descripción |
| :--- | :--- | :--- |
| **1991** | El inicio | Concebido por James Gosling y su equipo en **Sun Microsystems**. |
| **Origen** | Propósito Inicial | Se llamó originalmente **Oak**. Su objetivo eran los electrodomésticos inteligentes (tostadoras, microondas). |
| **1993-1995** | El giro a la Web | Ante el auge de Internet, el equipo adaptó la portabilidad de Java para ejecutar programas en navegadores. |
| **1995** | Salto a la fama | Renombrado oficialmente como **Java**. Su integración en **Netscape Navigator 2.0** permitió las primeras páginas web interactivas (Applets). |

---

## 3. Configuración del Entorno en VS Code
Guía paso a paso para preparar Visual Studio Code para el desarrollo en Java:

### Paso 1: Instalación de Extensiones
* Abrir la sección de **Extensions** en VS Code.
* Buscar e instalar el **"Extension Pack for Java"**. Este paquete incluye herramientas esenciales de lenguaje y depuración.

### Paso 2: Gestión del JDK (Java Development Kit)
* VS Code detectará automáticamente si tienes un JDK.
* Si no cuentas con uno, el editor facilitará la descarga. Se recomienda **OpenJDK 17** (versión de soporte a largo plazo).
* **Importante:** Asegurarse de configurar las **Variables de Entorno** del sistema para que Java sea reconocido globalmente.

### Paso 3: Prueba de Funcionamiento (Hola Mundo)
1. Crear una carpeta de proyecto.
2. Crear un archivo con extensión `.java` (ej. `HolaMundo.java`).
3. **Uso de Snippets:** Escribir `main` para generar el método principal y `sysout` para la sentencia de impresión de forma automática.
4. **Ejecución:** Hacer clic en el botón **"Run Java"** o usar la opción "Run" del editor. El resultado aparecerá en la terminal integrada.

### Paso 4: Recomendación Visual
* Se sugiere instalar la extensión **"Material Icon Theme"** para identificar mejor los archivos de Java y las carpetas de proyecto mediante iconos personalizados.
