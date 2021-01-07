package com.cloudogu.plantuml;

import com.google.inject.Provider;
import com.google.inject.util.Providers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.api.v2.resources.HalAppender;
import sonia.scm.api.v2.resources.HalEnricherContext;
import sonia.scm.api.v2.resources.ScmPathInfoStore;

import java.net.URI;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexLinkEnricherTest {

  private Provider<ScmPathInfoStore> scmPathInfoStoreProvider;

  @Mock
  private HalAppender appender;

  @BeforeEach
  public void setUp() {
    ScmPathInfoStore scmPathInfoStore = new ScmPathInfoStore();
    scmPathInfoStore.set(() -> URI.create("https://scm-manager.org/scm/api/"));
    scmPathInfoStoreProvider = Providers.of(scmPathInfoStore);
  }

  @Test
  void shouldAppendPlantUmlLink() {
    final IndexLinkEnricher indexLinkEnricher = new IndexLinkEnricher(scmPathInfoStoreProvider);
    HalEnricherContext context = HalEnricherContext.of();
    indexLinkEnricher.enrich(context, appender);
    verify(appender).appendLink("plantUml", "https://scm-manager.org/scm/api/v2/plantuml/svg/{content}");
  }

}
