package com.epikron.catzwiki.model

import com.epikron.catzwiki.ui.views.CatButtonView
import com.google.gson.annotations.SerializedName
import java.util.*

data class BreedModel(
    val adaptability: Int? = null,
    @SerializedName("affection_level")
    val affectionLevel: Int? = null,
    @SerializedName("alt_names")
    val altNames: String? = null,
    @SerializedName("cfa_url")
    val cfaUrl: String? = null,
    @SerializedName("child_friendly")
    val childFriendly: Int? = null,
    @SerializedName("country_code")
    val countryCode: String? = null,
    @SerializedName("country_codes")
    val countryCodes: String? = null,
    val description: String? = null,
    @SerializedName("dog_friendly")
    val dogFriendly: Int? = null,
    @SerializedName("energy_level")
    val energyLevel: Int? = null,
    val experimental: Int? = null,
    val grooming: Int? = null,
    val hairless: Int? = null,
    @SerializedName("health_issues")
    val healthIssues: Int? = null,
    val hypoallergenic: Int? = null,
    val id: String? = null,
    val image: SimpleImageModel? = null,
    val indoor: Int? = null,
    val intelligence: Int? = null,
    val lap: Int? = null,
    @SerializedName("life_span")
    val lifeSpan: String? = null,
    val name: String? = null,
    val natural: Int? = null,
    val origin: String? = null,
    val rare: Int? = null,
    @SerializedName("reference_image_id")
    val referenceImageId: String? = null,
    val rex: Int? = null,
    @SerializedName("shedding_level")
    val sheddingLevel: Int? = null,
    @SerializedName("short_legs")
    val shortLegs: Int? = null,
    @SerializedName("social_needs")
    val socialNeeds: Int? = null,
    @SerializedName("stranger_friendly")
    val strangerFriendly: Int? = null,
    @SerializedName("suppressed_tail")
    val suppressedTail: Int? = null,
    val temperament: String? = null,
    @SerializedName("vcahospitals_url")
    val vcaHospitalsUrl: String? = null,
    @SerializedName("vetstreet_url")
    val vetStreetUrl: String? = null,
    val vocalisation: Int? = null,
    val weight: WeightModel? = null,
    @SerializedName("wikipedia_url")
    val wikipediaUrl: String? = null
) {
    fun toCatButtonData() = CatButtonView.CatButtonViewData(
        name = this.name,
        description = this.description,
        countryCode = this.countryCode,
        countryName = this.origin ?: Locale("", this.countryCode ?: "").displayCountry,
        temperament = this.temperament,
        imageRes = this.image?.url
    )
}
