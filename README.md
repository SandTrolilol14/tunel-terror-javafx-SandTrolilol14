# 📻 Sala 2 · Radio fantasma

**Túnel del terror «El aula maldita»** · Desarrollo de Interfaces (2.º DAM) · Letra que entrega: **I**

Una radio antigua que lleva desde 1984 emitiendo en una frecuencia que nadie debería sintonizar. El visitante llega con la frecuencia que ha descubierto en la página HTML de 1.º, gira el dial hasta encontrarla y, cuando lo consigue, la emisora le habla… y le entrega la letra.

| Al empezar | Buscando la frecuencia | Sintonizada |
| :---: | :---: | :---: |
| ![Radio al arrancar](docs/capturas/1-inicio.png) | ![Dial en 89.4 MHz](docs/capturas/2-buscando.png) | ![Susto controlado al sintonizar](docs/capturas/3-susto.png) |

## 🔑 Pista acordada con 1.º

| | |
| --- | --- |
| **Pantalla de 1.º (HTML)** | Página de una emisora abandonada con la frecuencia oculta |
| **Pista** | La frecuencia **66.6 MHz** |
| **Qué hace el visitante en JavaFX** | Gira el dial (`Slider`) hasta que el display marca 66.6 MHz |
| **Letra que entrega** | **I** |

## 🎮 Cómo se juega

1. La radio muestra una instrucción clara: *«Gira el dial. Encuentra la frecuencia. Escúchala.»*
2. Al mover el dial, el display verde muestra la frecuencia, suena un chasquido y la barra de señal se llena cuanto más cerca está el visitante. Al mismo tiempo baja la estática y suben los susurros.
3. Al llegar a **66.6 MHz** (con una tolerancia de ±0.3) suena la voz fantasma, la radio tiembla, aparece una cara (susto controlado) y se revela la letra **I** en grande.
4. **A los 60 s** sin resolverlo aparece una pista: el rango de la frecuencia y el aviso de que el dial «miente».
5. **A los 90 s** la emisora «se apiada»: entrega la letra, sin susto.
6. Tras mostrar la letra, la sala **se reinicia sola** para el siguiente grupo. También se reinicia con **F12**.

## ▶️ Cómo ejecutarlo

**Requisitos:** JDK 21 y Maven 3.9 o superior (o IntelliJ IDEA, que trae Maven incluido).

```bash
git clone https://github.com/SandTrolilol14/tunel-terror-javafx-SandTrolilol14.git
cd tunel-terror-javafx-SandTrolilol14
mvn clean javafx:run
```

En IntelliJ: pulsa **Ctrl dos veces**, escribe `mvn clean javafx:run` y pulsa Enter.

| Tecla | Acción |
| --- | --- |
| **F12** | Reinicia la sala para el siguiente grupo |
| **Ctrl + Shift + Q** | Cierra la aplicación (solo el equipo; `Esc` no saca de pantalla completa) |

La aplicación arranca en pantalla completa, sin aviso y sin salida accidental (`setFullScreenExitHint("")` y `setFullScreenExitKeyCombination(KeyCombination.NO_MATCH)`). El título de la ventana muestra la constante `PROPIETARIO`.

## 🗂️ Estructura del proyecto

```
src/main/java/es/medac/sandtrolilol14/tunel/
├── App.java                  Arranque, pantalla completa, teclas F12 y Ctrl+Shift+Q
├── SalaController.java       Conecta la vista con la lógica, el sonido y los efectos
├── estilo/Fuentes.java       Carga las fuentes con Font.loadFont
├── logica/Sintonizador.java  Dial invertido, intensidad de la señal y pista (Java puro)
├── logica/RelojPiedad.java   Cuentas atrás de 60 s y 90 s (PauseTransition)
├── sonido/GestorSonido.java  Bucles con MediaPlayer y efectos con AudioClip
└── efectos/Efectos.java      Animaciones con Transition, sin destellos
src/main/resources/es/medac/sandtrolilol14/tunel/
├── sala.fxml                 Vista (Scene Builder)
├── estilos.css               Tema oscuro de radio antigua
├── fuentes/                  Creepster y Share Tech Mono (+ OFL.txt)
├── audio/                    Ambiente, estática, susurros, voz, chasquido y susto
└── img/susto.png             Cara del susto controlado
```

## 🧠 Reflexión de usabilidad

### Qué principio rompemos a propósito

Rompemos el principio de **correspondencia entre el sistema y el mundo real** (2.ª heurística de Nielsen) y el de **consistencia y estándares** (4.ª heurística). En cualquier radio, o en cualquier `Slider`, girar a la derecha **sube** el valor. En nuestra radio el dial está **invertido**: hacia la derecha la frecuencia **baja**. Además, el dial **no tiene marcas ni números**, así que no hay forma de saber a priori dónde está 66.6.

### Por qué da miedo

El visitante siente que **la radio no le obedece**, como si algo la estuviera controlando desde dentro. Lo que debería ser un gesto automático (girar hacia el número que busca) le aleja de él. Esa pérdida de control, unida a la estática que no se va y a los susurros que aparecen cuanto más se acerca, genera la tensión que buscamos.

### Por qué se entiende en 5 segundos

- Solo hay **un control** con el que interactuar: el dial.
- La instrucción, con buen contraste, dice exactamente qué hacer.
- La **barra de señal** y el **sonido** (menos estática y más susurros) indican en todo momento si el visitante se acerca, aunque el dial «mienta».
- Siempre se puede ganar: pista a los 60 s y letra a los 90 s.

### Cómo lo corregiríamos en una aplicación real

- Dial en el sentido estándar: a la derecha sube la frecuencia.
- Marcas y números en el dial (`showTickMarks` y `showTickLabels`) y un valor mínimo y máximo visibles.
- Un campo de texto para escribir la frecuencia directamente y botones de ajuste fino (±0.1).
- Accesibilidad: control con el teclado (flechas), contraste suficiente y una alternativa visual al feedback sonoro.

### Seguridad

- **Sin destellos ni efectos estroboscópicos.** Solo se animan la opacidad, la escala y la posición, y ninguna opacidad alterna más rápido de una vez cada 1,5 s. No hay pantallazos blancos.
- El susto es **controlado**: la cara aparece por escala y fundido, se mantiene 1,5 s y se desvanece. Solo ocurre al acertar; si la sala se apiada, no hay susto.
- **Volumen** limitado a un máximo de 0.8 en todos los sonidos.

## 👥 Equipo y reparto

Trabajamos con `main` / `develop`, una rama `feature/...` por tarea y Pull Requests revisados antes de fusionarse en `develop`.

| Parte | Rama | GitHub                                             |
| --- | --- |----------------------------------------------------|
| Coordinación, estructura base, integración (`SalaController`) y README | `feature/estructura-base`, `feature/integracion` | [Sandro](https://github.com/SandTrolilol14)        |
| Vista FXML con Scene Builder | `feature/vista-fxml` | [Jose Luis Mba](https://github.com/joseluismba60-ops) |
| Estilo CSS y fuentes | `feature/estilo-css` | [Jose Luis](https://github.com/jmb0049-code)     |
| Lógica del dial y relojes de piedad | `feature/logica-dial` | [Salva](https://github.com/ZedCrimson)         |
| Sonido | `feature/sonido-v2` | [Jose María](https://github.com/jmmyuso)    |
| Animaciones y susto controlado | `feature/efectos` | [Antonio](https://github.com/ajesusgg2007-ai)       |

## 📜 Créditos de recursos

### Fuentes

| Fuente | Autor | Licencia | Fuente |
| --- | --- | --- | --- |
| Creepster | Font Diner, Inc. | SIL Open Font License 1.1 | [Google Fonts](https://fonts.google.com/specimen/Creepster) |
| Share Tech Mono | Carrois Type Design | SIL Open Font License 1.1 | [Google Fonts](https://fonts.google.com/specimen/Share+Tech+Mono) |

El texto completo de la licencia está en `src/main/resources/es/medac/sandtrolilol14/tunel/fuentes/OFL.txt`.

### Audio

| Archivo | Uso | Original | Autor | Licencia y URL |
| --- | --- | --- | --- | --- |
| `ambiente.mp3` | Ambiente en bucle | *Drone Texture* (619960) | soundreality |  |
| `estatica.mp3` | Estática de radio | *Radio Static 2* (26831) | freesound_community |  |
| `susurros.mp3` | Susurros al acercarse | *Strange Whispers* (415245) | u_5hx6qi66bg |  |
| `chasquido.mp3` | Clic del dial | *Mouse Click SFX* (444806) | Dragon Studio |  |
| `susto.mp3` | Golpe del susto | *Horror Impact Hit* (567238) | Universfield |  |
| `voz.mp3` | Voz fantasma | Voz sintética «Dalia» (Loquendo) | — |  |

### Imagen

| Archivo | Uso | Autor / fuente | Licencia |
| --- | --- | --- | --- |
| `img/susto.png` | Cara del susto controlado |  |  |

## 🤖 Uso de IA

Cada miembro probó y revisó su parte antes de integrarla.
