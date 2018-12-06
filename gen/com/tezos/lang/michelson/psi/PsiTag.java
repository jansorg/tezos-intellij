// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.tezos.lang.michelson.lang.tag.TagMetadata;

public interface PsiTag extends PsiData {

  @NotNull
  List<PsiData> getDataList();

  @NotNull
  List<PsiEmptyBlock> getEmptyBlockList();

  @Nullable
  PsiElement getTagToken();

  @NotNull
  String getTagName();

  @Nullable
  TagMetadata getTagMetadata();

}
