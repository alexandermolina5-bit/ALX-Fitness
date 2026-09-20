package com.alx.fitness

data class FoodItem(
    val name: String,
    val portion: String,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val kcal: Int
)

data class Meal(
    val title: String,
    val foods: List<FoodItem>
) {
    val kcal get() = foods.sumOf { it.kcal }
    val protein get() = foods.sumOf { it.protein }
    val carbs get() = foods.sumOf { it.carbs }
    val fats get() = foods.sumOf { it.fats }
}

data class NutritionDay(
    val name: String,
    val meals: List<Meal>
) {
    val kcal get() = meals.sumOf { it.kcal }
    val protein get() = meals.sumOf { it.protein }
    val carbs get() = meals.sumOf { it.carbs }
    val fats get() = meals.sumOf { it.fats }
}

object ALXNutrition {
    private fun f(n:String,p:String,pr:Int,c:Int,fa:Int,k:Int)=FoodItem(n,p,pr,c,fa,k)

    val week = listOf(
        NutritionDay("Lunes", listOf(
            Meal("Desayuno", listOf(f("Avena","60 g",8,40,4,230),f("Yogur natural","170 g",10,8,4,110),f("Banano","1 mediano",1,27,0,105))),
            Meal("Almuerzo", listOf(f("Pollo a la plancha","150 g",45,0,6,250),f("Arroz","1 taza",4,45,1,205),f("Ensalada mixta","2 tazas",3,12,5,100))),
            Meal("Merienda", listOf(f("Manzana","1",0,25,0,95),f("Nueces","20 g",4,4,13,140))),
            Meal("Cena", listOf(f("Huevos","3",18,2,15,215),f("Frijoles","3/4 taza",11,30,1,170),f("Vegetales","2 tazas",4,15,2,90)))
        )),
        NutritionDay("Martes", listOf(
            Meal("Desayuno", listOf(f("Huevos","2",12,1,10,145),f("Pan integral","2 rebanadas",7,28,2,160),f("Papaya","1 taza",1,16,0,65))),
            Meal("Almuerzo", listOf(f("Pescado","160 g",36,0,8,230),f("Papa cocida","250 g",5,50,0,220),f("Vegetales","2 tazas",4,15,2,90))),
            Meal("Merienda", listOf(f("Yogur natural","170 g",10,8,4,110),f("Avena","30 g",4,20,2,115))),
            Meal("Cena", listOf(f("Pollo","140 g",42,0,6,235),f("Tortillas de maíz","3",5,36,3,190),f("Pico de gallo","1 taza",2,10,0,50)))
        )),
        NutritionDay("Miércoles", listOf(
            Meal("Desayuno", listOf(f("Gallo pinto","1 taza",12,55,6,320),f("Huevo","2",12,1,10,145))),
            Meal("Almuerzo", listOf(f("Carne magra","150 g",38,0,10,255),f("Arroz","1 taza",4,45,1,205),f("Vegetales","2 tazas",4,15,2,90))),
            Meal("Merienda", listOf(f("Banano","1",1,27,0,105),f("Maní","20 g",5,4,10,120))),
            Meal("Cena", listOf(f("Atún","1 lata",30,0,2,145),f("Papa","200 g",4,40,0,175),f("Ensalada","2 tazas",3,12,5,100)))
        )),
        NutritionDay("Jueves", listOf(Meal("Desayuno",listOf(f("Avena con leche","1 tazón",16,58,9,380))),Meal("Almuerzo",listOf(f("Pollo con arroz y vegetales","1 plato",48,62,12,560))),Meal("Merienda",listOf(f("Yogur y fruta","1 porción",12,32,4,210))),Meal("Cena",listOf(f("Tortilla de huevo y ensalada","1 plato",28,22,18,370))))),
        NutritionDay("Viernes", listOf(Meal("Desayuno",listOf(f("Gallo pinto y huevo","1 plato",24,58,16,480))),Meal("Almuerzo",listOf(f("Pescado, papa y ensalada","1 plato",42,52,10,470))),Meal("Merienda",listOf(f("Banano y maní","1 porción",7,31,10,225))),Meal("Cena",listOf(f("Pollo y frijoles","1 plato",48,38,10,450))))),
        NutritionDay("Sábado", listOf(Meal("Desayuno",listOf(f("Pan integral, huevos y fruta","1 plato",24,48,15,430))),Meal("Almuerzo",listOf(f("Carne magra con arroz","1 plato",44,58,14,540))),Meal("Merienda",listOf(f("Yogur con avena","1 porción",14,30,6,230))),Meal("Cena",listOf(f("Atún con tortillas y ensalada","1 plato",38,42,9,410))))),
        NutritionDay("Domingo", listOf(Meal("Desayuno",listOf(f("Avena, banano y nueces","1 tazón",14,62,15,430))),Meal("Almuerzo",listOf(f("Casado de pollo balanceado","1 plato",48,70,15,620))),Meal("Merienda",listOf(f("Fruta y yogur","1 porción",10,30,4,195))),Meal("Cena",listOf(f("Huevos, frijoles y vegetales","1 plato",32,42,18,470)))))
    )

    // Generic targets for app UX; not medical prescriptions.
    fun calorieBand(goal:String): String = when(goal) {
        "Perder grasa" -> "Déficit moderado personalizado"
        "Aumentar masa muscular" -> "Superávit moderado y proteína suficiente"
        "Tonificar" -> "Mantenimiento ajustado a actividad"
        else -> "Mantenimiento según actividad"
    }
}
