package com.example.bloomkitchen.data.datasouce.menu

import com.example.bloomkitchen.data.model.Menu

class DummyMenuDataSource : MenuDataSource {
    override fun getMenuList(): List<Menu> {
        return mutableListOf(
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_ayambakar.jpg?raw=true",
                name = "Ayam Bakar",
                price = 18000.0,
                desc = "Potongan ayam dimarinasi dengan rempah-rempah khas, dipanggang hingga kecokelatan, disajikan dengan nasi putih dan lalapan.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_ayamgeprek.jpg?raw=true",
                name = "Ayam Geprek",
                price = 15000.0,
                desc = "Potongan ayam goreng yang dihancurkan dan disajikan dengan sambal pedas, sering kali disertai dengan nasi putih dan lalapan.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_nasi_goreng.jpg?raw=true",
                name = "Nasi Goreng",
                price = 15000.0,
                desc = "Nasi yang digoreng bersama bumbu-bumbu seperti bawang, bawang putih, kecap, dan rempah lainnya, dicampur dengan potongan daging ayam dan telur.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_roti_bakar.jpg?raw=true",
                name = "Roti Bakar",
                price = 12000.0,
                desc = "Roti yang dipanggang atau digoreng hingga kecokelatan, disajikan dengan tambahan topping seperti keju, selai kacang, atau cokelat.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_ayamgoreng.jpg?raw=true",
                name = "Ayam Goreng",
                price = 17000.0,
                desc = "Potongan ayam yang digoreng hingga kecokelatan, disajikan dengan nasi putih dan lalapan.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
            Menu(
                imgUrl = "https://github.com/bellafebrianyn/BloomKitchen-assets/blob/main/menu_img/img_ayamopor.jpg?raw=true",
                name = "Ayam Opor",
                price = 16000.0,
                desc = "Hidangan ayam yang dimasak dalam kuah santan kental yang kaya rempah-rempah, seperti lengkuas, kunyit, dan serai, memberikan cita rasa gurih dan lezat.",
                location = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                googleMapsLink = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
            ),
        )
    }
}