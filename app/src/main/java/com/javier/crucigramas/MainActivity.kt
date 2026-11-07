package com.javier.crucigramas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

private val ink = Color(0xFF292929)
private val blue = Color(0xFF9FD7FA)
private data class Word(val answer:String,val clue:String)
private val bank = listOf(
 Word("ARTE","Una de las bellas artes"),Word("PINTURA","Arte de representar con colores"),
 Word("MUSICA","Arte de combinar sonidos"),Word("DANZA","Arte del movimiento"),
 Word("TEATRO","Arte de representar historias"),Word("POESIA","Expresión literaria en verso"),
 Word("ESCULTURA","Arte de modelar figuras"),Word("CINE","Séptimo arte"),
 Word("NOVELA","Obra literaria extensa"),Word("LIRICA","Género literario subjetivo"),
 Word("SOL","Estrella del sistema solar"),Word("LUNA","Satélite natural de la Tierra"),
 Word("MAR","Gran masa de agua salada"),Word("RIO","Corriente natural de agua"),
 Word("MONTE","Elevación natural del terreno"),Word("NUBE","Masa visible de gotas"),
 Word("VIENTO","Aire en movimiento"),Word("NIEVE","Agua congelada en copos"),
 Word("GATO","Felino doméstico"),Word("PERRO","Animal doméstico fiel"),
 Word("CABALLO","Animal usado para montar"),Word("AGUILA","Ave rapaz"),
 Word("DELFIN","Mamífero marino"),Word("ABEJA","Insecto que produce miel"),
 Word("ROBLE","Árbol de madera fuerte"),Word("ROSA","Flor con espinas"),
 Word("PINO","Árbol de hojas perennes"),Word("AZUL","Color del cielo despejado"),
 Word("VERDE","Color de la hierba"),Word("ROJO","Color de una fresa"),
 Word("ENERO","Primer mes del año"),Word("MARZO","Mes que inicia la primavera"),
 Word("JULIO","Mes típico de verano"),Word("OCHO","Dos veces cuatro"),
 Word("NUEVE","Uno menos que diez"),Word("DOCE","Tres veces cuatro")
)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{ CrosswordApp() }}
}
@Composable private fun CrosswordApp(){
 var tab by remember{mutableIntStateOf(0)}
 var level by remember{mutableStateOf("Fácil")}
 var id by remember{mutableIntStateOf(1)}
 var index by remember{mutableIntStateOf(0)}
 var typed by remember{mutableStateOf("")}
 val pool=remember(level){bank.filter{when(level){"Fácil"->it.answer.length in 3..5;"Medio"->it.answer.length in 5..7;else->it.answer.length>=6}}}
 val words=remember(id,level){pool.shuffled(kotlin.random.Random(id*31L+level.length)).take(7)}
 val word=words.getOrNull(index%words.size)?:bank.first()
 Column(Modifier.fillMaxSize().background(Color(0xFFF1F1F1))){
  Row(Modifier.fillMaxWidth().background(ink).padding(18.dp),verticalAlignment=Alignment.CenterVertically){
   Text("☰",color=Color.White,fontSize=24.sp);Spacer(Modifier.width(18.dp))
   Text("Crucigramas",color=Color.White,fontSize=25.sp,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("🛒",fontSize=22.sp)
  }
  Row(Modifier.fillMaxWidth().background(ink)){
   listOf("LISTA","ESTADÍSTICAS").forEachIndexed{n,s->Column(Modifier.weight(1f).clickable{tab=n}.padding(top=10.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(s,color=Color.White);Spacer(Modifier.height(8.dp));Box(Modifier.fillMaxWidth().height(3.dp).background(if(tab==n)Color(0xFFFFD42A)else ink))}}
  }
  if(tab==1){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){Text("Estadísticas",fontSize=24.sp,fontWeight=FontWeight.Bold);listOf("Crucigramas resueltos","Letras resueltas sin ayuda","Mejor resultado","Peor resultado","Media").forEach{Card(Modifier.fillMaxWidth()){Text("$it     —",Modifier.padding(18.dp))}}}}
  else{
   Row(Modifier.fillMaxWidth().padding(10.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Fácil","Medio","Difícil").forEach{FilterChip(selected=level==it,onClick={level=it;index=0;typed=""},label={Text(it)})}}
   Text("Crucigrama nº $id · $level",Modifier.padding(start=16.dp,bottom=8.dp),color=Color.DarkGray)
   LazyVerticalGrid(columns=GridCells.Fixed(9),modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp).weight(1f),userScrollEnabled=false){
    items((0 until 81).toList()){cell->val r=cell/9;val c=cell%9;val active=r<words.size && c in 1..7
     Box(Modifier.aspectRatio(1f).padding(.5.dp).background(if(active)Color.White else ink).clickable{if(active){index=r%words.size;typed=""}},contentAlignment=Alignment.Center){if(active && c==4)Text(words[r].answer.getOrNull((c-1).coerceAtMost(words[r].answer.lastIndex))?.toString()?: "",fontWeight=FontWeight.Bold)}
    }
   }
   Row(Modifier.fillMaxWidth().background(blue).clickable{index=(index+1)%words.size;typed=""}.padding(16.dp),verticalAlignment=Alignment.CenterVertically){Text(word.clue,Modifier.weight(1f),fontSize=20.sp,color=ink);Text("›",fontSize=30.sp,color=Color.White)}
   Text("＿ ".repeat(word.answer.length),Modifier.fillMaxWidth().padding(8.dp),textAlign=TextAlign.Center,fontSize=22.sp)
   Text(typed,Modifier.fillMaxWidth(),textAlign=TextAlign.Center,color=Color.Gray)
   listOf("QWERTYUIOP","ASDFGHJKLÑ","ZXCVBNM").forEach{row->Row(Modifier.fillMaxWidth().padding(horizontal=8.dp),horizontalArrangement=Arrangement.spacedBy(4.dp)){row.forEach{ch->Button(onClick={typed+=ch},modifier=Modifier.weight(1f).height(42.dp),contentPadding=PaddingValues(0.dp),colors=ButtonDefaults.buttonColors(containerColor=Color.White,contentColor=ink)){Text(ch.toString())}}}}
   Row(Modifier.fillMaxWidth().padding(8.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedButton(onClick={typed=word.answer.take(1)},modifier=Modifier.weight(1f)){Text("Resolver letra")};OutlinedButton(onClick={index=(index+1)%words.size;typed=""},modifier=Modifier.weight(1f)){Text("Siguiente")}}
   Button(onClick={id++;index=0;typed=""},modifier=Modifier.fillMaxWidth().padding(horizontal=12.dp),colors=ButtonDefaults.buttonColors(containerColor=ink)){Text("＋ Nuevo crucigrama")}
  }
 }
}
