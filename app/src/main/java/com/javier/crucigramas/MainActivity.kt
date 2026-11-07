package com.javier.crucigramas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val Navy = Color(0xFF202329)
private val Paper = Color(0xFFF3F4F6)
private val Sky = Color(0xFF9BD5F5)
private val Yellow = Color(0xFFFFD447)
private val Ink = Color(0xFF25282D)
private data class Entry(val answer:String,val clue:String,val hard:Int=1)
private data class Placed(val entry:Entry,val cells:List<Pair<Int,Int>>,val number:Int)
private data class Puzzle(val number:Long,val across:Placed,val downs:List<Placed>) {
    val all get() = listOf(across)+downs
    fun target(r:Int,c:Int):Char? = all.firstNotNullOfOrNull { w -> val i=w.cells.indexOf(r to c); if(i<0)null else w.entry.answer[i] }
}
private val words = listOf(
Entry("PLAYA","Orilla de arena junto al mar"),Entry("TRAMA","Argumento de una historia",2),Entry("CLAVO","Pieza metálica para unir"),Entry("GRANO","Semilla pequeña de cereal"),Entry("PLATO","Recipiente para comer"),
Entry("CIELO","Espacio donde vemos las nubes"),Entry("SUELO","Superficie que pisamos"),Entry("VUELO","Acción de desplazarse por el aire",2),Entry("FUEGO","Combustión que produce luz y calor"),Entry("NUEVO","Que no está usado"),Entry("BUENO","Que tiene buena calidad"),Entry("HUEVO","Alimento que ponen las aves"),
Entry("CHICO","Muchacho o niño"),Entry("CLIMA","Condiciones habituales del tiempo",2),Entry("GRITO","Voz dada con fuerza"),Entry("TRIGO","Cereal con el que se hace harina"),Entry("BRISA","Viento suave"),Entry("PRIMA","Hija de un tío o una tía"),
Entry("BARCO","Vehículo que navega"),Entry("CARTA","Mensaje escrito o menú"),Entry("TARDE","Parte del día después del mediodía"),Entry("VERDE","Color de las hojas"),Entry("PERRO","Animal doméstico que ladra"),Entry("TORRE","Construcción alta"),Entry("CERCA","A poca distancia"),Entry("FIRMA","Nombre escrito como rúbrica",2),
Entry("BROMA","Dicho o hecho para hacer reír"),Entry("MOTOR","Máquina que produce movimiento",2),Entry("COCHE","Vehículo de cuatro ruedas"),Entry("COSTA","Terreno junto al mar"),Entry("DOLOR","Sensación desagradable del cuerpo"),Entry("COLOR","Cualidad visual de las cosas"),Entry("TOMAR","Coger o beber algo"),Entry("COMER","Ingerir alimentos"),
Entry("CASAS","Edificios para vivir"),Entry("MESAS","Muebles con tablero y patas"),Entry("RISAS","Expresiones de alegría"),Entry("COSAS","Objetos o asuntos"),Entry("PESCA","Actividad de capturar peces",2),Entry("PASTA","Alimento hecho con harina"),
Entry("METRO","Unidad de longitud o transporte urbano"),Entry("PATIO","Espacio abierto de una casa"),Entry("GATOS","Felinos domésticos"),Entry("MITAD","Una de dos partes iguales"),Entry("BOTAS","Calzado que cubre el tobillo"),Entry("VOTAR","Emitir un voto",2),Entry("RITMO","Orden regular de sonidos",2),Entry("DATOS","Información o hechos"),
Entry("CACAO","Semilla usada para hacer chocolate"),Entry("NACER","Comenzar a vivir"),Entry("VACAS","Hembras adultas del ganado bovino"),Entry("TACOS","Comida mexicana o piezas de madera"),Entry("ROCAS","Fragmentos naturales de piedra"),Entry("PECES","Animales que viven en el agua"),
Entry("GEMAS","Piedras preciosas",2),Entry("TEMOR","Sensación de miedo",2),Entry("RIMAS","Coincidencias de sonidos finales",2),Entry("CAPAS","Prendas o estratos superpuestos",2),Entry("TAPAS","Cubiertas o pequeñas raciones"),Entry("LAPIZ","Útil para escribir y dibujar"),Entry("MAPAS","Representaciones de lugares"),Entry("COPAS","Vasos con pie o trofeos"),
Entry("RADIO","Medio de comunicación sonora"),Entry("MEDIO","Punto entre dos extremos",2),Entry("MODOS","Maneras de hacer algo",2),Entry("NADAR","Avanzar por el agua"),Entry("PODER","Tener capacidad para hacer algo",2),
Entry("NUNCA","En ningún momento"),Entry("CINTA","Tira larga y estrecha"),Entry("PUNTO","Signo que cierra una oración"),Entry("TINTA","Líquido para escribir"),Entry("RONDA","Vuelta o recorrido",2),Entry("LINDO","Bonito o agradable"),
Entry("MANGO","Fruta tropical o asa"),Entry("PAGAR","Entregar dinero por algo"),Entry("LAGOS","Grandes masas de agua interior"),Entry("MAGIA","Arte de crear ilusiones",2),
Entry("CAUSA","Motivo por el que ocurre algo"),Entry("PAUSA","Interrupción breve"),Entry("FAUNA","Conjunto de animales de una zona",2),Entry("SAUNA","Baño de calor y vapor",2),Entry("JAULA","Estructura para alojar animales"),
Entry("NUBES","Masas visibles de vapor de agua"),Entry("SUBIR","Ir hacia un lugar más alto"),Entry("CUBOS","Recipientes o cuerpos de seis caras"),Entry("RUBIO","De cabello claro"),Entry("MAYOR","De más edad o tamaño"),
Entry("ARENA","Conjunto de granos de roca"),Entry("SALTO","Acción de elevarse y caer"),Entry("PALMA","Árbol tropical o parte de la mano"),Entry("CALLE","Vía pública entre edificios"),Entry("FALDA","Prenda de vestir"),Entry("BALON","Pelota usada en muchos deportes"),
Entry("MUNDO","Planeta Tierra o universo"),Entry("NOTAS","Apuntes o sonidos musicales"),Entry("RATON","Pequeño roedor o dispositivo"),Entry("PIANO","Instrumento musical de teclas"),Entry("LISTA","Relación ordenada de elementos"),Entry("SERIE","Conjunto de capítulos o elementos"),Entry("NORTE","Punto cardinal opuesto al sur"),Entry("SABOR","Sensación que produce un alimento"),Entry("VALOR","Cualidad de quien afronta el peligro",2),Entry("PARED","Elemento vertical de una habitación"),Entry("CAMPO","Terreno fuera de la ciudad"),Entry("FRESA","Fruta roja pequeña")
)
private val acrossWords = listOf("PLAYA","CIELO","PERRO","MOTOR","CASAS","SALTO","ROCAS","TEMOR","RADIO","PUNTO","GRITO","CAUSA","SUBIR","CUBOS","TRIGO","BRISA","PRIMA","MUNDO","ARENA","NOTAS","RATON","PIANO","LISTA","SERIE","NORTE","SABOR","VALOR","PARED","CAMPO","FRESA")

private fun fingerprint(p:Puzzle)=p.across.entry.answer+":"+p.downs.joinToString(","){it.entry.answer}
private fun makePuzzle(id:Long,level:String,used:Set<String>):Puzzle {
    val maxHard=when(level){"Fácil"->1;"Medio"->2;else->3}
    val pool=words.filter{it.hard<=maxHard}
    val byMiddle=pool.groupBy{it.answer[2]}
    val rng=Random(id xor (level.hashCode().toLong() shl 17))
    val candidates=acrossWords.mapNotNull{a->words.firstOrNull{it.answer==a}}.filter{a->a.answer.all{byMiddle[it]?.isNotEmpty()==true}}.shuffled(rng)
    for(a in candidates){
        val downs=a.answer.map{ch->(byMiddle[ch]?:emptyList()).filter{it.answer!=a.answer}.shuffled(rng).firstOrNull()}
        if(downs.all{it!=null}){
            val p=Puzzle(id,Placed(a,(0..4).map{3 to (it+1)},1),downs.mapIndexed{i,e->Placed(e!!,(0..4).map{(it+1) to (i+1)},i+2)})
            if(fingerprint(p) !in used)return p
        }
    }
    val a=candidates.firstOrNull()?:words.first{it.answer=="PLAYA"}
    val ds=a.answer.mapIndexed{i,ch->(byMiddle[ch]?:words.filter{it.answer[2]==ch}).firstOrNull()?:words.first{it.answer[2]==ch}}
    return Puzzle(id,Placed(a,(0..4).map{3 to (it+1)},1),ds.mapIndexed{i,e->Placed(e,(0..4).map{(it+1) to (i+1)},i+2)})
}

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);window.statusBarColor=android.graphics.Color.rgb(32,35,41);window.navigationBarColor=android.graphics.Color.rgb(32,35,41);setContent{CrosswordApp(this)}}
}
@Composable private fun CrosswordApp(context:Context){
 val prefs=remember{context.getSharedPreferences("crossword_stats",Context.MODE_PRIVATE)}
 var tab by remember{mutableIntStateOf(0)}
 var level by remember{mutableStateOf(prefs.getString("level","Fácil")?:"Fácil")}
 var number by remember{mutableLongStateOf(prefs.getLong("number",1L))}
 var solved by remember{mutableIntStateOf(prefs.getInt("solved",0))}
 var hints by remember{mutableIntStateOf(prefs.getInt("hints",0))}
 var selected by remember{mutableIntStateOf(0)}
 var cursor by remember{mutableIntStateOf(0)}
 var message by remember{mutableStateOf("¡A jugar!")}
 var showClues by remember{mutableStateOf(false)}
 val used=remember{(prefs.getStringSet("used",emptySet())?:emptySet()).toMutableSet()}
 var puzzle by remember{mutableStateOf(makePuzzle(number,level,used))}
 val letters=remember{mutableStateMapOf<String,Char>()}
 val active=puzzle.all.getOrNull(selected)?:puzzle.across
 val activeCell=active.cells.getOrNull(cursor)?:active.cells.first()
 fun persist(){prefs.edit().putLong("number",number).putString("level",level).putInt("solved",solved).putInt("hints",hints).putStringSet("used",used.toSet()).apply()}
 fun nextPuzzle(){
    var candidate: Puzzle
    var tries=0
    do { number=(number+1).coerceAtMost(1_000_000_000_000L);candidate=makePuzzle(number,level,used);tries++ } while(fingerprint(candidate) in used && tries<300)
    puzzle=candidate;used.add(fingerprint(candidate));letters.clear();selected=0;cursor=0;message="Nuevo crucigrama";persist()
 }
 fun typeLetter(ch:Char){val cell=active.cells.getOrNull(cursor)?:return;letters["${cell.first},${cell.second}"]=ch;if(cursor<active.cells.lastIndex)cursor++;message=""}
 fun check(){
    val wrong=puzzle.all.sumOf{w->w.cells.countIndexed{i,cell->letters["${cell.first},${cell.second}"]?.let{it!=w.entry.answer[i]}==true}}
    val complete=puzzle.all.all{w->w.cells.all{letters["${it.first},${it.second}"]!=null}}
    if(complete&&wrong==0){solved++;message="¡Perfecto! Crucigrama resuelto";persist()}else message=if(wrong>0)"Hay $wrong letras incorrectas" else "Aún quedan casillas"
 }
 MaterialTheme(colorScheme=lightColorScheme(primary=Navy,secondary=Sky,background=Paper)){
  Column(Modifier.fillMaxSize().background(Paper)){
   Row(Modifier.fillMaxWidth().background(Navy).padding(horizontal=18.dp,vertical=15.dp),verticalAlignment=Alignment.CenterVertically){
    Text("✳",color=Yellow,fontSize=25.sp);Spacer(Modifier.width(12.dp));Text("Crucigramas",color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("⋮",color=Color.White,fontSize=25.sp)
   }
   Row(Modifier.fillMaxWidth().background(Navy)){listOf("LISTA","ESTADÍSTICAS").forEachIndexed{i,label->Column(Modifier.weight(1f).clickable{tab=i}.padding(top=9.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(label,color=Color.White,fontSize=12.sp,fontWeight=FontWeight.SemiBold);Spacer(Modifier.height(9.dp));Box(Modifier.fillMaxWidth().height(3.dp).background(if(tab==i)Yellow else Navy))}}}
   if(tab==1){
    LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
     item{Text("Estadísticas",fontSize=25.sp,fontWeight=FontWeight.Bold,color=Ink)}
     item{StatCard("Crucigramas resueltos",solved.toString())};item{StatCard("Pistas utilizadas",hints.toString())};item{StatCard("Crucigrama actual","#${number}")};item{StatCard("Dificultad",level)}
     item{OutlinedButton(onClick={solved=0;hints=0;persist()},modifier=Modifier.fillMaxWidth()){Text("Restablecer estadísticas")}}
    }
   }else{
    Row(Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=6.dp),horizontalArrangement=Arrangement.spacedBy(6.dp)){
     listOf("Fácil","Medio","Difícil").forEach{d->FilterChip(selected=level==d,onClick={level=d;prefs.edit().putString("level",d).apply();puzzle=makePuzzle(number,level,used);letters.clear();selected=0;cursor=0},label={Text(d)})}
     Spacer(Modifier.weight(1f));Text("#${number}",color=Color.Gray,modifier=Modifier.align(Alignment.CenterVertically),fontSize=12.sp)
    }
    Row(Modifier.fillMaxWidth().padding(horizontal=16.dp),verticalAlignment=Alignment.CenterVertically){Text("HORIZONTAL 1  ·  VERTICALES 2–6",fontSize=10.sp,color=Color.Gray,fontWeight=FontWeight.Bold);Spacer(Modifier.weight(1f));Text(level.uppercase(),fontSize=10.sp,color=Navy,fontWeight=FontWeight.Bold)}
    LazyVerticalGrid(columns=GridCells.Fixed(7),modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=7.dp).weight(1f),userScrollEnabled=false){
     items((0 until 49).toList()){n->
      val r=n/7;val c=n%7;val target=puzzle.target(r,c);val key="${r},${c}";val value=letters[key];val isActive=active.cells.contains(r to c)
      Box(Modifier.aspectRatio(1f).padding(1.dp).background(if(target==null)Color(0xFF30343A)else if(isActive)Color(0xFFD9F0FF)else Color.White).border(if(isActive)2.dp else .6.dp,if(isActive)Color(0xFF3998D2)else Color(0xFFB8BEC6).copy(alpha=.8f)).clickable{
       if(target!=null){val match=puzzle.all.indexOfFirst{it.cells.contains(r to c)};if(match>=0){selected=match;cursor=puzzle.all[match].cells.indexOf(r to c)}}
      },contentAlignment=Alignment.Center){
       if(target!=null){if(r==3&&c==1)Text("1",modifier=Modifier.align(Alignment.TopStart).padding(start=2.dp),fontSize=7.sp,color=Color.Gray);Text(value?.toString()?:"",fontSize=19.sp,fontWeight=FontWeight.Bold,color=Ink)}
      }
     }
    }
    Row(Modifier.fillMaxWidth().background(Sky).clickable{showClues=true}.padding(horizontal=15.dp,vertical=10.dp),verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f)){Text(if(selected==0)"1 · HORIZONTAL" else "${active.number} · VERTICAL",fontSize=10.sp,fontWeight=FontWeight.Bold,color=Color(0xFF38627B));Text(active.entry.clue,fontSize=15.sp,color=Ink,fontWeight=FontWeight.Medium)}
     Text("${cursor+1}/${active.cells.size}  ›",fontSize=13.sp,color=Ink)
    }
    if(message.isNotBlank())Text(message,Modifier.fillMaxWidth().padding(top=3.dp),textAlign=TextAlign.Center,color=Color(0xFF52606B),fontSize=11.sp)
    listOf("QWERTYUIOP","ASDFGHJKLÑ","ZXCVBNM").forEach{row->Row(Modifier.fillMaxWidth().padding(horizontal=5.dp,vertical=2.dp),horizontalArrangement=Arrangement.spacedBy(3.dp)){row.forEach{ch->Surface(onClick={typeLetter(ch)},modifier=Modifier.weight(1f).height(37.dp),shape=RoundedCornerShape(6.dp),color=Color.White,shadowElevation=1.dp){Box(contentAlignment=Alignment.Center){Text(ch.toString(),color=Ink,fontSize=15.sp,fontWeight=FontWeight.SemiBold)}}}}}
    Row(Modifier.fillMaxWidth().padding(horizontal=8.dp,vertical=3.dp),horizontalArrangement=Arrangement.spacedBy(5.dp)){
     OutlinedButton(onClick={val cell=active.cells.getOrNull(cursor);if(cell!=null){letters["${cell.first},${cell.second}"]=active.entry.answer[cursor];hints++;persist();if(cursor<active.cells.lastIndex)cursor++}},modifier=Modifier.weight(1f),contentPadding=PaddingValues(horizontal=3.dp)){Text("Pista",fontSize=11.sp)}
     OutlinedButton(onClick={letters.remove("${activeCell.first},${activeCell.second}");message="Casilla borrada"},modifier=Modifier.weight(1f),contentPadding=PaddingValues(horizontal=3.dp)){Text("Borrar",fontSize=11.sp)}
     OutlinedButton(onClick={check},modifier=Modifier.weight(1f),contentPadding=PaddingValues(horizontal=3.dp)){Text("Comprobar",fontSize=11.sp)}
    }
    Button(onClick={nextPuzzle},modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=4.dp).height(43.dp),colors=ButtonDefaults.buttonColors(containerColor=Navy),shape=RoundedCornerShape(10.dp)){Text("＋  NUEVO CRUCIGRAMA",fontWeight=FontWeight.Bold)}
   }
  }
  if(showClues)AlertDialog(onDismissRequest={showClues=false},title={Text("Lista de pistas")},text={LazyColumn{items(puzzle.all.size){i->val w=puzzle.all[i];Text("${w.number}. ${if(i==0)"Horizontal" else "Vertical"} · ${w.entry.clue}",Modifier.fillMaxWidth().clickable{selected=i;cursor=0;showClues=false}.padding(8.dp),color=Ink)}}},confirmButton={TextButton(onClick={showClues=false}){Text("Cerrar")}})
 }
}
@Composable private fun StatCard(label:String,value:String){Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=Color.White)){Row(Modifier.fillMaxWidth().padding(17.dp),verticalAlignment=Alignment.CenterVertically){Text(label,modifier=Modifier.weight(1f),color=Ink,fontSize=15.sp);Text(value,color=Navy,fontWeight=FontWeight.Bold,fontSize=17.sp)}}}
private inline fun <T> Iterable<T>.countIndexed(predicate:(Int,T)->Boolean):Int{var n=0;forEachIndexed{i,v->if(predicate(i,v))n++};return n}
