package com.tezos.lang.michelson.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.tezos.lang.michelson.ui.Icons
import javax.swing.Icon

/**
 * The page for Michelson colors settings in the settings dialog.
 *
 * @author jansorg
 */
class MichelsonColorSettingsPage : ColorSettingsPage {
    private companion object {
        // handle highlighting applied by annotators
        private val additionalTags: Map<String, TextAttributesKey> = mapOf(
                "ANNOTATION_TYPE" to MichelsonSyntaxHighlighter.TYPE_ANNOTATION,
                "ANNOTATION_VAR" to MichelsonSyntaxHighlighter.VARIABLE_ANNOTATION,
                "ANNOTATION_FIELD" to MichelsonSyntaxHighlighter.FIELD_ANNOTATION
        )

        /**
         *  Handle token based highlighting.
         *  A double slash // creates nested groups of color definitions in the settings UI.
         */
        private val attrs: Array<AttributesDescriptor> = arrayOf(
                AttributesDescriptor("General//Line comment", MichelsonSyntaxHighlighter.LINE_COMMENT),
                AttributesDescriptor("General//Block comment", MichelsonSyntaxHighlighter.BLOCK_COMMENT),
                AttributesDescriptor("General//Section name", MichelsonSyntaxHighlighter.SECTION_NAME),
                AttributesDescriptor("General//Tag", MichelsonSyntaxHighlighter.TAG),

                AttributesDescriptor("Types//Type name", MichelsonSyntaxHighlighter.TYPE_NAME),
                AttributesDescriptor("Types//Comparable type name", MichelsonSyntaxHighlighter.TYPE_NAME_COMPARABLE),

                AttributesDescriptor("Annotations//Annotation", MichelsonSyntaxHighlighter.ANNOTATION),
                AttributesDescriptor("Annotations//Type annotation", MichelsonSyntaxHighlighter.TYPE_ANNOTATION),
                AttributesDescriptor("Annotations//Variable annotation", MichelsonSyntaxHighlighter.VARIABLE_ANNOTATION),
                AttributesDescriptor("Annotations//Field / constructor annotation", MichelsonSyntaxHighlighter.FIELD_ANNOTATION),

                AttributesDescriptor("Instructions//Instruction", MichelsonSyntaxHighlighter.INSTRUCTION),
                AttributesDescriptor("Instructions//Macro instruction", MichelsonSyntaxHighlighter.MACRO),

                AttributesDescriptor("Literals//Number literal", MichelsonSyntaxHighlighter.INT_LITERAL),
                AttributesDescriptor("Literals//String literal", MichelsonSyntaxHighlighter.STRING_LITERAL),
                AttributesDescriptor("Literals//Byte literal", MichelsonSyntaxHighlighter.BYTE_LITERAL),
                AttributesDescriptor("Literals//Boolean literal", MichelsonSyntaxHighlighter.BOOLEAN_LITERAL),
                AttributesDescriptor("Literals//Unit literal", MichelsonSyntaxHighlighter.UNIT_LITERAL),

                AttributesDescriptor("Misc//Semicolon", MichelsonSyntaxHighlighter.SEMI),
                AttributesDescriptor("Misc//Parentheses", MichelsonSyntaxHighlighter.PAREN),
                AttributesDescriptor("Misc//Braces", MichelsonSyntaxHighlighter.BRACES)
        )
    }

    override fun getDisplayName(): String = "Michelson"

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attrs

    override fun getColorDescriptors(): Array<ColorDescriptor> = emptyArray()

    override fun getHighlighter(): SyntaxHighlighter = MichelsonSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap() = additionalTags

    override fun getIcon(): Icon = Icons.Tezos

    override fun getDemoText(): String {
        return """
            /* This is a smart contract written in Michelson */
            parameter (pair (signature <ANNOTATION_FIELD>%signed_weather_data</ANNOTATION_FIELD>) (nat <ANNOTATION_TYPE>:rain</ANNOTATION_TYPE> <ANNOTATION_FIELD>%actual_level</ANNOTATION_FIELD>));
            # (pair (under_key over_key) (pair weather_service_key (pair rain_level days_in_future)))
            storage (pair (pair (contract <ANNOTATION_FIELD>%under_key unit</ANNOTATION_FIELD>)
                                (contract <ANNOTATION_FIELD>%over_key unit</ANNOTATION_FIELD>))
                          (pair (nat <ANNOTATION_TYPE>:rain</ANNOTATION_TYPE> <ANNOTATION_FIELD>%rain_level</ANNOTATION_FIELD>) (key <ANNOTATION_FIELD>%weather_service_key</ANNOTATION_FIELD>)));
            code { DUP; DUP;
                   CAR; MAP_CDR{PACK ; BLAKE2B};
                   SWAP; CDDDR <ANNOTATION_FIELD>%weather_service_key</ANNOTATION_FIELD>;
                   DIP {UNPAIR} ; CHECK_SIGNATURE @sigok; # Check if the data has been correctly signed
                   ASSERT; # If signature is not correct, end the execution
                   DUP; DUP; DUP; DIIIP{CDR <ANNOTATION_FIELD>%storage</ANNOTATION_FIELD>}; # Place storage type on bottom of stack
                   DIIP{CDAR};                # Place contracts below numbers
                   DIP{CADR <ANNOTATION_FIELD>%actual_level</ANNOTATION_FIELD>};   # Get actual rain
                   CDDAR <ANNOTATION_FIELD>%rain_level</ANNOTATION_FIELD>;         # Get rain threshold
                   CMPLT; IF {CAR <ANNOTATION_FIELD>%under_key</ANNOTATION_FIELD>} {CDR <ANNOTATION_FIELD>%over_key</ANNOTATION_FIELD>};     # Select contract to receive tokens
                   BALANCE; UNIT ; TRANSFER_TOKENS <ANNOTATION_TYPE>@trans.op</ANNOTATION_TYPE>; # Setup and execute transfer
                   NIL operation ; SWAP ; CONS ;
                   PAIR };
        """.trimIndent()
    }
}