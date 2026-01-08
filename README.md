# MyPlaceholder 🌟

**MyPlaceholder** es un plugin potente y versátil para Minecraft que permite a los administradores crear sus propios placeholders personalizados de forma dinámica. ¡Dale vida a tus mensajes, menús y tablas con lógica personalizada y condiciones avanzadas!

---

## 🔗 Enlaces Oficiales
* **Perfil del Creador (BuiltByBit):** https://builtbybit.com/creators/nutellim.455402
* **Wiki Oficial (Documentación):** https://nute-setups.gitbook.io/docs/plugins/myplaceholder
* **Discord Oficial (Soporte):** https://discord.com/invite/ZermkrzMDg
* **Repositorio GitHub:** https://github.com/pgprimitz/MyPlaceholder

---

## ✨ Características Principales
* **Compatibilidad Universal:** Funciona perfectamente desde la **1.16.1 hasta la 1.21.x** gracias a su compilación optimizada en Java 8.
* **Sistema de Lógica:** Realiza comparaciones matemáticas y de texto en tiempo real.
* **Formato de Colores:** Soporte completo para colores Legacy (`&6`) y códigos **Hexadecimales** (`&#FF3D00`).
* **Integración Adventure:** Soporte nativo para **MiniMessage** y componentes de texto modernos para una experiencia visual superior.
* **Placeholders Dinámicos:** Soporte para argumentos externos usando `{0}`, `{1}`, etc., permitiendo placeholders interactivos.
* **Condicionales Avanzados:** Muestra diferentes resultados basados en si el jugador cumple o no ciertos requisitos, como balance de economía o permisos.

---

## 🛠️ Configuración y Uso

### Carpeta de Placeholders
El plugin carga automáticamente todos los archivos `.yml` dentro de la carpeta `/placeholders/`. Esto te permite organizar tus creaciones por categorías (ej: `rangos.yml`, `stats.yml`).

### Tipos de Placeholders Disponibles
Puedes configurar diversos comportamientos según tus necesidades:

#### 1. Texto con Colores (`COLORED_TEXT`)
Ideal para nombres de servidor o etiquetas estéticas con degradados.
```yaml
ejemplo-titulo:
  value: "&#61FF57&lSERVER &8| &7¡Bienvenido!"
  type: "COLORED_TEXT"
```

#### 2. Mensajes Aleatorios (RANDOM)
Selecciona una línea al azar de una lista cada vez que el placeholder se refresca, ideal para anuncios.
```yaml
mensaje-bienvenida:
  values:
    - "¡Hola de nuevo!"
    - "¡Esperamos que te diviertas!"
    - "¡Visita nuestra tienda!"
  type: "RANDOM"
```

#### 3. Con Requisitos (REQUIREMENT)
Permite crear lógica condicional comparando un input (usualmente otro placeholder) contra un output.
Operadores: >=, <=, ==, !=, string equals, string contains.
```yaml
rango-vip:
  value: "&a[VIP]"
  type: "REQUIREMENT"
  requirements:
    check_perm:
      type: "=="
      input: "%luckperms_has_permission_group.vip%"
      output: "yes"
      deny: "&7[Usuario]"
```

#### ⌨️ Comandos y Permisos
Los mensajes de los comandos son totalmente editables desde el config.yml:
| Comando | Descripción | Permiso |
| :--- | :--- | :--- |
| `/mp help` | Muestra el menú de ayuda del plugin. | `myplaceholder.help` |
| `/mp reload` | Recarga la configuración y los placeholders. | `myplaceholder.reload` |
| `/mp info` | Muestra información detallada del plugin. | `myplaceholder.info` |
| `/mp list` | Muestra una lista de todos los placeholders cargados. | `myplaceholder.list` |

#### 🚀 Instalación
1. Descarga el archivo MyPlaceholder-1.0.0.jar.
2. Asegúrate de tener instalado PlaceholderAPI en tu servidor.
3. Sube el plugin a tu carpeta /plugins/.
4. Reinicia el servidor para generar los archivos de configuración iniciales.
5. ¡Empieza a crear tus placeholders en la carpeta /placeholders/!

#### 📂 Archivos de Sistema
 - config.yml: Controla el prefijo del plugin y los mensajes de administración.
 - example.yml: Archivo de ejemplo con comentarios detallados para aprender a usar todas las funciones.

Desarrollado con ❤️ por Risas | Distribuido por nutellim
