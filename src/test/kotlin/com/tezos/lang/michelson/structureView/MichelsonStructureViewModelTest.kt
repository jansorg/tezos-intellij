package com.tezos.lang.michelson.structureView

import com.intellij.testFramework.PlatformTestUtil
import com.tezos.lang.michelson.MichelsonFixtureTest

/**
 * @author jansorg
 */
class MichelsonStructureViewModelTest : MichelsonFixtureTest() {
    fun testStrucutureView() {
        configureByCode("CDR; NIL operation; PAIR;")

        assertStructureView("""
            -file
             -Contract
              +Section parameter
              +Section storage
              +Section code""".trimIndent())

        assertTreeStructure("""
            MICHELSON_FILE
             PsiElement(CONTRACT)
              PsiElement(SECTION)
              PsiElement(SECTION)
              PsiElement(SECTION)""".trimIndent())
    }

    fun testNestedContractStructureView() {
        configureByCode {
            """
           DROP; NIL int; # starting storage for contract
           AMOUNT;                   # Push the starting balance
           PUSH bool False;          # Not spendable
           DUP;                      # Or delegatable
           NONE key_hash;                 # No delegate
           PUSH key_hash "tz1cxcwwnzENRdhe2Kb8ZdTrdNy4bFNyScx5";
           CREATE_CONTRACT          # Create the contract
             { parameter (list int) ;
               storage (list int) ;
               code
                 { CAR;
                   MAP {PUSH int 1; ADD};
                   NIL operation;
                   PAIR } };
           NIL operation; SWAP; CONS; PAIR} # Ending calling convention stuff
        """.trimIndent()
        }


        assertStructureView("""
            -file
             -Contract
              +Section parameter
              +Section storage
              +Section code""".trimIndent())

        assertTreeStructure("""
            MICHELSON_FILE
             PsiElement(CONTRACT)
              PsiElement(SECTION)
              PsiElement(SECTION)
              PsiElement(SECTION)
               PsiElement(CREATE_CONTRACT_INSTRUCTION)
                PsiElement(CONTRACT)
                 PsiElement(SECTION)
                 PsiElement(SECTION)
                 PsiElement(SECTION)""".trimIndent())
    }

    private fun assertTreeStructure(expected: String) {
        myFixture.testStructureView { view ->
            PlatformTestUtil.assertTreeStructureEquals(view.treeStructure, expected + "\n")
        }
    }

    private fun assertStructureView(expected: String) {
        myFixture.testStructureView { view ->
            PlatformTestUtil.assertTreeEqual(view.tree, expected + "\n")
        }
    }
}