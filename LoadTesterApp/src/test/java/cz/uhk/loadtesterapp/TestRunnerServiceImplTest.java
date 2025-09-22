//package cz.uhk.loadtesterapp;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import cz.uhk.loadtesterapp.model.*;
//import cz.uhk.loadtesterapp.repository.TestRepository;
//import cz.uhk.loadtesterapp.service.ApiRequestService;
//import cz.uhk.loadtesterapp.service.TestRunnerServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InOrder;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit testy pro TestRunnerServiceImpl:
// * - Bez Spring kontextu
// * - ApiRequestService a TestRepository jsou mocky
// * - CancellationRegistry je mock (bez zrušení)
// */
//@ExtendWith(MockitoExtension.class)
//class TestRunnerServiceImplTest {
//
//    @Mock private ApiRequestService api;
//    @Mock private TestRepository repo;
//    @Mock private CancellationRegistry cancels;
//
//    private TestRunnerServiceImpl service;
//
//    @BeforeEach
//    void setUp() {
//        service = new TestRunnerServiceImpl(api, repo, new ObjectMapper(), cancels);
//    }
//
//    @Test
//    void run_happyPath_transitionsAndSavesSummary() {
//        // given
//        TestRun entity = new TestRun();
//        entity.setId(10L);
//        entity.setStatus(TestStatus.APPROVED);
//        entity.setTotalRequests(3);
//        entity.setConcurrency(2);
//        entity.setRequest(RequestDefinition.builder()
//                .url("http://sut.local/api/foo")
//                .method(HttpMethodType.GET)
//                .build());
//
//        when(cancels.isCancelRequested(10L)).thenReturn(false);
//        when(repo.findById(10L)).thenReturn(Optional.of(entity));
//
//        // API odpovědi – vždy 200 OK
//        when(api.send(any(RequestDefinition.class)))
//                .thenReturn(Mono.just(ResponseEntity.ok("{\"serverProcessingMs\":12,\"queueWaitMs\":3}")));
//
//        // snapshot kontroly při samotném volání save(...)
//        java.util.concurrent.atomic.AtomicInteger calls = new java.util.concurrent.atomic.AtomicInteger(0);
//        when(repo.save(any(TestRun.class))).thenAnswer(inv -> {
//            TestRun tr = inv.getArgument(0);
//            int n = calls.incrementAndGet();
//            if (n == 1) {
//                // první uložení: RUNNING + startedAt + effectiveUrl
//                org.junit.jupiter.api.Assertions.assertEquals(TestStatus.RUNNING, tr.getStatus());
//                org.junit.jupiter.api.Assertions.assertNotNull(tr.getStartedAt());
//                org.junit.jupiter.api.Assertions.assertNotNull(tr.getEffectiveUrl());
//            } else if (n == 2) {
//                // druhé uložení: FINISHED + summary + finishedAt
//                org.junit.jupiter.api.Assertions.assertEquals(TestStatus.FINISHED, tr.getStatus());
//                org.junit.jupiter.api.Assertions.assertNotNull(tr.getFinishedAt());
//                org.junit.jupiter.api.Assertions.assertNotNull(tr.getSummary());
//            } else {
//                org.junit.jupiter.api.Assertions.fail("Unexpected extra save() call #" + n);
//            }
//            return tr; // simulace DB – vrací uloženou entitu
//        });
//
//        // when
//        Mono<TestRun> mono = service.run(10L);
//
//        // then
//        reactor.test.StepVerifier.create(mono)
//                .assertNext(saved -> {
//                    // finální kontrola výsledku
//                    org.assertj.core.api.Assertions.assertThat(saved.getStatus()).isEqualTo(TestStatus.FINISHED);
//                    org.assertj.core.api.Assertions.assertThat(saved.getSummary()).isNotNull();
//                    org.assertj.core.api.Assertions.assertThat(saved.getSummary().getSuccesses()).isEqualTo(3);
//                })
//                .verifyComplete();
//
//        // volání závislostí
//        org.mockito.Mockito.verify(repo, org.mockito.Mockito.times(1)).findById(10L);
//        org.mockito.Mockito.verify(repo, org.mockito.Mockito.times(2)).save(org.mockito.ArgumentMatchers.any(TestRun.class));
//        org.mockito.Mockito.verify(api, org.mockito.Mockito.times(3)).send(org.mockito.ArgumentMatchers.any(RequestDefinition.class));
//        org.mockito.Mockito.verifyNoMoreInteractions(repo, api);
//    }
//
//
//    @Test
//    void run_wrongStatus_throwsIllegalState() {
//        // given: test není v APPROVED/WAITING
//        TestRun entity = new TestRun();
//        entity.setId(20L);
//        entity.setStatus(TestStatus.CREATED);
//
//        when(repo.findById(20L)).thenReturn(Optional.of(entity));
//
//        // when
//        Mono<TestRun> mono = service.run(20L);
//
//        // then
//        StepVerifier.create(mono)
//                .expectErrorSatisfies(err ->
//                        assertThat(err)
//                                .isInstanceOf(IllegalStateException.class)
//                                .hasMessageContaining("APPROVED/WAITING"))
//                .verify();
//
//        verify(repo, times(1)).findById(20L);
//        verifyNoMoreInteractions(repo);
//        verifyNoInteractions(api);
//    }
//
//    @Test
//    void run_canceled_setsFailedAndFallbackSummary() {
//        // given
//        TestRun entity = new TestRun();
//        entity.setId(30L);
//        entity.setStatus(TestStatus.APPROVED);
//        entity.setTotalRequests(2);
//        entity.setConcurrency(1);
//        entity.setRequest(RequestDefinition.builder()
//                .url("http://sut.local/api/error")
//                .method(HttpMethodType.GET)
//                .build());
//
//        when(cancels.isCancelRequested(30L)).thenReturn(true);
//        when(repo.findById(30L)).thenReturn(Optional.of(entity));
//
//        // POZOR: ŽÁDNÉ when(api.send(...)) — k volání nedojde
//
//        // snapshot při save(): RUNNING -> FAILED + fallback
//        var calls = new java.util.concurrent.atomic.AtomicInteger(0);
//        when(repo.save(any(TestRun.class))).thenAnswer(inv -> {
//            TestRun tr = inv.getArgument(0);
//            int n = calls.incrementAndGet();
//            if (n == 1) {
//                org.junit.jupiter.api.Assertions.assertEquals(TestStatus.RUNNING, tr.getStatus());
//            } else if (n == 2) {
//                org.junit.jupiter.api.Assertions.assertEquals(TestStatus.FAILED, tr.getStatus());
//                org.junit.jupiter.api.Assertions.assertNotNull(tr.getSummary());
//            } else {
//                org.junit.jupiter.api.Assertions.fail("Unexpected extra save() call #" + n);
//            }
//            return tr;
//        });
//
//        // when
//        var mono = service.run(30L);
//
//        // then
//        reactor.test.StepVerifier.create(mono)
//                .assertNext(saved -> {
//                    org.assertj.core.api.Assertions.assertThat(saved.getStatus()).isEqualTo(TestStatus.FAILED);
//                    org.assertj.core.api.Assertions.assertThat(saved.getSummary()).isNotNull();
//                })
//                .verifyComplete();
//
//        verify(repo, times(1)).findById(30L);
//        verify(repo, times(2)).save(any(TestRun.class));
//        verifyNoInteractions(api);               // <- žádné volání API v cancel scénáři
//        verifyNoMoreInteractions(repo);
//    }
//
//
//
//}