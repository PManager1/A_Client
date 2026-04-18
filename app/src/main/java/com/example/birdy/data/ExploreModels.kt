package com.example.birdy.data

data class ExploreCategory(
    val title: String,
    val imageName: String
)

object ExploreData {

    val categories: List<Pair<ExploreCategory, ExploreCategory>> = listOf(
        // Row 1
        ExploreCategory("Fast Food", "FastFood") to
                ExploreCategory("Pizza", "Pizza"),
        // Row 2
        ExploreCategory("Burgers", "Burger") to
                ExploreCategory("Desserts", "Cheesecake"),
        // Row 3
        ExploreCategory("Mexican", "Mexican") to
                ExploreCategory("Indian", "Indian-food"),
        // Row 4
        ExploreCategory("Chicken", "Chicken") to
                ExploreCategory("Chinese", "Chinese"),
        // Row 5
        ExploreCategory("Sandwich", "Sandwich") to
                ExploreCategory("Seafood", "https://www.eatwell101.com/wp-content/uploads/2021/06/Skillet-Garlic-Shrimp-recipe.jpg"),
        // Row 6
        ExploreCategory("Comfort food", "https://hips.hearstapps.com/del.h-cdn.co/assets/18/05/1600x2399/gallery-1517424535-delish-white-chicken-chili-1.jpg?resize=768:*") to
                ExploreCategory("Peruvian", "https://onestophalal.com/cdn/shop/articles/peruvian_recipes_1200x.jpg?v=1749542945"),
        // Row 7
        ExploreCategory("Thai", "https://images.squarespace-cdn.com/content/v1/5d3d5921fa823600016c24ba/5ddbbdf2-bbf1-4158-8ab2-d30ccb6ed271/Thai+Red+Curry.png?format=1000w") to
                ExploreCategory("Ethiopian", "https://gradfood.com/wp-content/uploads/2020/02/Traditional-Ethiopian-Meal-scaled.jpg"),
        // Row 8
        ExploreCategory("Salad", "https://cdn.loveandlemons.com/wp-content/uploads/2020/05/spring-salad.jpg") to
                ExploreCategory("Soup", "https://www.gimmesomeoven.com/wp-content/uploads/2018/10/Autumn-Wild-Rice-Soup-Recipe-4.jpg"),
        // Row 9
        ExploreCategory("Latin American", "https://static.vecteezy.com/system/resources/previews/054/355/099/large_2x/delicious-assortment-of-traditional-latin-american-cuisine-free-photo.jpg") to
                ExploreCategory("Italian", "https://c.ndtvimg.com/2021-04/umk8i7ko_pasta_625x300_01_April_21.jpg?im=FaceCrop,algorithm=dnn,width=620,height=350"),
        // Row 10
        ExploreCategory("Ramen & Pho", "https://res-4.cloudinary.com/dostuff-media/image/upload/w_1200,q_75,c_limit,f_auto/v1506982712/page-image-10205-5558d314-c5ef-4de1-b3b8-579e3fa5de6a.jpg") to
                ExploreCategory("African", "https://blackrestaurantweeks.com/wp-content/uploads/2021/10/taste-of-nigera-photo-1.jpeg"),
        // Row 11
        ExploreCategory("Steak", "https://ichef.bbci.co.uk/food/ic/food_16x9_1600/recipes/rib-eye_steak_with_61963_16x9.jpg") to
                ExploreCategory("Sushi", "https://facts.net/wp-content/uploads/2021/05/Set-of-sushi-and-maki.jpg"),
        // Row 12
        ExploreCategory("Southern", "https://i0.wp.com/duesouth.media/wp-content/uploads/2019/03/iu-26.jpeg?fit=600%2C389&ssl=1") to
                ExploreCategory("Greek", "https://onegirlwholeworld.com/wp-content/uploads/2023/07/greek_food_IMG_6666.jpg"),
        // Row 13
        ExploreCategory("Bubble Tea", "https://assets.epicurious.com/photos/5953ca064919e41593325d97/1:1/w_3744,h_3744,c_limit/bubble_tea_recipe_062817.jpg") to
                ExploreCategory("Vegan", "https://www.feastingathome.com/wp-content/uploads/2022/04/Coconut-Rice-Bowl-12.jpg"),
        // Row 14
        ExploreCategory("Asian", "https://images.squarespace-cdn.com/content/v1/5e06519ef43bd75de7803760/bc8c2c82-089a-4f03-960b-3cb6a3ef4790/Top+100+Asian+Street+Food?format=2500w") to
                ExploreCategory("Smoothie", "https://static01.nyt.com/images/2025/02/25/multimedia/Strawberry-Smoothie-cqzb/Strawberry-Smoothie-cqzb-mediumSquareAt3X.jpg"),
        // Row 15
        ExploreCategory("Barbeque", "https://www.licious.in/blog/wp-content/uploads/2020/12/Chicken-Barbeque-Kebab.jpg") to
                ExploreCategory("Filipino", "https://www.saveur.com/uploads/2019/03/18/3VV2L3S2XIVDITRHZKG2XB6YTQ.jpg?dpr=2&format=auto&optimize=high&width=1200"),
        // Row 16
        ExploreCategory("Japanese", "https://storage.googleapis.com/birdyimages/__App/japanese.webp") to
                ExploreCategory("Snacks", "https://storage.googleapis.com/birdyimages/__App/snacks.jpg"),
        // Row 17
        ExploreCategory("Noodles", "https://blog.themalamarket.com/wp-content/uploads/2024/06/Vegetarian-pulled-noodles-lead-more-sat.jpg") to
                ExploreCategory("Coffee", "Coffee"),
        // Row 18
        ExploreCategory("Bagels", "https://bakingamoment.com/wp-content/uploads/2020/06/IMG_8831-new-york-bagel-recipe.jpg") to
                ExploreCategory("Korean", "https://storage.googleapis.com/birdyimages/__App/Korean.jpg"),
        // Row 19
        ExploreCategory("Spanish", "https://storage.googleapis.com/birdyimages/__App/spanish.jpg") to
                ExploreCategory("American", "https://usafoodblog.com/wp-content/uploads/2025/09/what-are-some-american-foods.webp"),
        // Row 20
        ExploreCategory("Australian", "https://storage.googleapis.com/birdyimages/__App/Australian.jpg") to
                ExploreCategory("Halal", "https://img.lb.wbmdstatic.com/vim/live/webmd/consumer_assets/site_images/article_thumbnails/BigBead/what_is_halal_bigbead/1800x1200_getty_rf_what_is_halal_bigbead.jpg?resize=750px:*&output-quality=75"),
        // Row 21
        ExploreCategory("Poutineries", "https://storage.googleapis.com/birdyimages/__App/Poutine.jpg") to
                ExploreCategory("European", "https://storage.googleapis.com/birdyimages/__App/european-food.webp"),
        // Row 22
        ExploreCategory("French", "https://storage.googleapis.com/birdyimages/__App/french.jpeg") to
                ExploreCategory("Irish", "https://storage.googleapis.com/birdyimages/__App/Irish-Breakfast.jpg"),
        // Row 23
        ExploreCategory("Kosher", "https://storage.googleapis.com/birdyimages/__App/kosher.webp") to
                ExploreCategory("Tapas", "https://storage.googleapis.com/birdyimages/__App/Tapas.jpg"),
        // Row 24
        ExploreCategory("Vietnamese", "https://storage.googleapis.com/birdyimages/__App/vietnames.jpeg") to
                ExploreCategory("Russian", "https://storage.googleapis.com/birdyimages/__App/russian.webp"),
        // Row 25
        ExploreCategory("Poke", "https://storage.googleapis.com/birdyimages/__App/poke.webp") to
                ExploreCategory("Bakery", "https://storage.googleapis.com/birdyimages/__App/Bakery.webp"),
        // Row 26
        ExploreCategory("Argentine", "https://storage.googleapis.com/birdyimages/__App/Argentina.jpg") to
                ExploreCategory("Middle Eastern", "https://storage.googleapis.com/birdyimages/__App/middeleEast.webp")
    )

    /** Flat list for grid layout */
    val flatCategories: List<ExploreCategory>
        get() = categories.flatMap { listOf(it.first, it.second) }
}