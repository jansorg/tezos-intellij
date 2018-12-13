// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;

public class PsiVisitor<R> extends PsiElementVisitor {

  public R visitAnnotation(@NotNull PsiAnnotation o) {
    return visitMichelsonComposite(o);
  }

  public R visitAnnotationList(@NotNull PsiAnnotationList o) {
    return visitMichelsonComposite(o);
  }

  public R visitBlockInstruction(@NotNull PsiBlockInstruction o) {
    return visitInstruction(o);
  }

  public R visitCodeSection(@NotNull PsiCodeSection o) {
    return visitSection(o);
  }

  public R visitComplexType(@NotNull PsiComplexType o) {
    return visitType(o);
  }

  public R visitContract(@NotNull PsiContract o) {
    return visitMichelsonComposite(o);
  }

  public R visitContractWrapper(@NotNull PsiContractWrapper o) {
    return visitMichelsonComposite(o);
  }

  public R visitCreateContractInstruction(@NotNull PsiCreateContractInstruction o) {
    return visitGenericInstruction(o);
  }

  public R visitData(@NotNull PsiData o) {
    return visitMichelsonComposite(o);
  }

  public R visitDataList(@NotNull PsiDataList o) {
    return visitData(o);
  }

  public R visitDataMap(@NotNull PsiDataMap o) {
    return visitData(o);
  }

  public R visitEmptyBlock(@NotNull PsiEmptyBlock o) {
    return visitBlockInstruction(o);
  }

  public R visitFieldAnnotation(@NotNull PsiFieldAnnotation o) {
    return visitAnnotation(o);
  }

  public R visitGenericInstruction(@NotNull PsiGenericInstruction o) {
    return visitInstruction(o);
  }

  public R visitInstruction(@NotNull PsiInstruction o) {
    return visitMichelsonComposite(o);
  }

  public R visitLiteralData(@NotNull PsiLiteralData o) {
    return visitData(o);
  }

  public R visitMacroInstruction(@NotNull PsiMacroInstruction o) {
    return visitInstruction(o);
  }

  public R visitMapEntry(@NotNull PsiMapEntry o) {
    return visitTag(o);
  }

  public R visitSection(@NotNull PsiSection o) {
    return visitMichelsonComposite(o);
  }

  public R visitSimpleType(@NotNull PsiSimpleType o) {
    return visitType(o);
  }

  public R visitStringLiteral(@NotNull PsiStringLiteral o) {
    return visitLiteralData(o);
  }

  public R visitTag(@NotNull PsiTag o) {
    return visitData(o);
  }

  public R visitType(@NotNull PsiType o) {
    return visitMichelsonComposite(o);
  }

  public R visitTypeAnnotation(@NotNull PsiTypeAnnotation o) {
    return visitAnnotation(o);
  }

  public R visitTypeSection(@NotNull PsiTypeSection o) {
    return visitSection(o);
  }

  public R visitVariableAnnotation(@NotNull PsiVariableAnnotation o) {
    return visitAnnotation(o);
  }

  public R visitMichelsonComposite(@NotNull MichelsonComposite o) {
    visitElement(o);
    return null;
  }

}
