package com.hyunec.pickuprequest.infrastructure.mysql.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "label_image")
@Entity
class LabelImageJpaEntity(
    val url: String,
) : BaseEntity()
