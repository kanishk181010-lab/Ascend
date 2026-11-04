package com.example

import com.example.data.model.AscensionTier
import com.example.data.protocols.LooksmaxxingProtocols
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun `verify ascension tiers mapping`() {
    val tierNovice = AscensionTier.fromScore(20)
    assertEquals("Bloat Wanderer", tierNovice.name)

    val tierRising = AscensionTier.fromScore(45)
    assertEquals("Ascending Neophyte", tierRising.name)

    val tierSculptor = AscensionTier.fromScore(65)
    assertEquals("Chiseled Sculptor", tierSculptor.name)

    val tierMaster = AscensionTier.fromScore(95)
    assertEquals("Aesthetic Master", tierMaster.name)
  }

  @Test
  fun `verify protocols data structure`() {
    val protocols = LooksmaxxingProtocols.ALL_PROTOCOLS
    assertTrue("Should have multiple protocols", protocols.size >= 4)
    val debloat = protocols.first { it.id == "debloat_24h" }
    assertTrue(debloat.steps.isNotEmpty())
    assertEquals(1, debloat.steps.first().stepNumber)
  }
}
