//package cz.uhk.loadtesterapp;
//
//import cz.uhk.loadtesterapp.model.*;
//import cz.uhk.loadtesterapp.repository.TestRepository;
//import cz.uhk.loadtesterapp.service.TestCommandServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TestCommandServiceImplTest {
//
//    @Mock
//    private TestRepository repo;
//
//    @InjectMocks
//    private TestCommandServiceImpl service;
//
//    private TestRun createdEntity;
//
//    @BeforeEach
//    void init() {
//        createdEntity = new TestRun();
//        createdEntity.setId(1L);
//        createdEntity.setStatus(TestStatus.CREATED);
//        createdEntity.setTotalRequests(5);
//        createdEntity.setConcurrency(1);
//        createdEntity.setRequest(RequestDefinition.builder()
//                .url("http://sut/api")
//                .method(HttpMethodType.GET)
//                .build());
//    }
//
//    @Test
//    void create_setsStatusCreated_andCreatedAt_andSaves() {
//        TestRun toCreate = new TestRun();
//        when(repo.save(any(TestRun.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        TestRun saved = service.create(toCreate);
//
//        assertThat(saved.getStatus()).isEqualTo(TestStatus.CREATED);
//        assertThat(saved.getCreatedAt()).isNotNull();
//
//        ArgumentCaptor<TestRun> cap = ArgumentCaptor.forClass(TestRun.class);
//        verify(repo, times(1)).save(cap.capture());
//        TestRun persisted = cap.getValue();
//        assertThat(persisted.getStatus()).isEqualTo(TestStatus.CREATED);
//        assertThat(persisted.getCreatedAt()).isNotNull();
//
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void update_inCreated_updatesFields_andDoesNotCallSave() {
//        when(repo.findById(1L)).thenReturn(Optional.of(createdEntity));
//
//        TestUpdateRequest req = new TestUpdateRequest(
//                Integer.valueOf(20),
//                Integer.valueOf(4),
//                ProcessingMode.POOL,
//                Integer.valueOf(8), // může být i null, když nechceš měnit
//                RequestDefinition.builder()
//                        .url("http://sut/other")
//                        .method(HttpMethodType.POST)
//                        .contentType("application/json")
//                        .body("{\"x\":1}")
//                        .build(),
//                Long.valueOf(15)    // může být i null; typ musí být Long
//        );
//
//
//        Optional<TestRun> out = service.update(1L, req);
//
//        assertThat(out).isPresent();
//        TestRun updated = out.get();
//        assertThat(updated.getTotalRequests()).isEqualTo(20);
//        assertThat(updated.getConcurrency()).isEqualTo(4);
//        assertThat(updated.getProcessingMode()).isEqualTo(ProcessingMode.POOL);
//        assertThat(updated.getPoolSizeOrCap()).isEqualTo(8);
//        assertThat(updated.getDelayMs()).isEqualTo(15);
//        assertThat(updated.getRequest().getUrl()).isEqualTo("http://sut/other");
//        assertThat(updated.getRequest().getMethod()).isEqualTo(HttpMethodType.POST);
//
//        verify(repo, times(1)).findById(1L);
//        // v této implementaci update nevolá repo.save() (spoléhá na persistence context)
//        verify(repo, never()).save(any());
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void update_whenNotCreated_throwsIllegalState() {
//        TestRun notCreated = new TestRun();
//        notCreated.setId(2L);
//        notCreated.setStatus(TestStatus.APPROVED);
//        when(repo.findById(2L)).thenReturn(Optional.of(notCreated));
//
//        TestUpdateRequest req = new TestUpdateRequest(
//                Integer.valueOf(10),
//                Integer.valueOf(2),
//                ProcessingMode.SERIAL,
//                null, // poolSizeOrCap
//                RequestDefinition.builder().url("u").method(HttpMethodType.GET).build(),
//                null  // delayMs (Long)
//        );
//
//        assertThrows(IllegalStateException.class, () -> service.update(2L, req));
//
//        verify(repo, times(1)).findById(2L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void deleteById_exists_deletesAndReturnsTrue() {
//        when(repo.existsById(3L)).thenReturn(true);
//
//        boolean result = service.deleteById(3L);
//
//        assertThat(result).isTrue();
//        verify(repo, times(1)).existsById(3L);
//        verify(repo, times(1)).deleteById(3L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void deleteById_notExists_returnsFalseAndDoesNotDelete() {
//        when(repo.existsById(4L)).thenReturn(false);
//
//        boolean result = service.deleteById(4L);
//
//        assertThat(result).isFalse();
//        verify(repo, times(1)).existsById(4L);
//        verify(repo, never()).deleteById(anyLong());
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void approve_fromCreated_setsApproved() {
//        when(repo.findById(1L)).thenReturn(Optional.of(createdEntity));
//
//        Optional<TestRun> out = service.approve(1L);
//
//        assertThat(out).isPresent();
//        assertThat(out.get().getStatus()).isEqualTo(TestStatus.APPROVED);
//
//        verify(repo, times(1)).findById(1L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void approve_whenNotCreated_throws() {
//        TestRun r = new TestRun();
//        r.setId(5L);
//        r.setStatus(TestStatus.WAITING);
//        when(repo.findById(5L)).thenReturn(Optional.of(r));
//
//        assertThrows(IllegalStateException.class, () -> service.approve(5L));
//
//        verify(repo, times(1)).findById(5L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void reject_fromCreated_setsRejected() {
//        when(repo.findById(1L)).thenReturn(Optional.of(createdEntity));
//
//        Optional<TestRun> out = service.reject(1L);
//
//        assertThat(out).isPresent();
//        assertThat(out.get().getStatus()).isEqualTo(TestStatus.REJECTED);
//
//        verify(repo, times(1)).findById(1L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void reject_whenNotCreated_throws() {
//        TestRun r = new TestRun();
//        r.setId(6L);
//        r.setStatus(TestStatus.APPROVED);
//        when(repo.findById(6L)).thenReturn(Optional.of(r));
//
//        assertThrows(IllegalStateException.class, () -> service.reject(6L));
//
//        verify(repo, times(1)).findById(6L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void cancel_fromCreated_setsCancelled_andFinishedAt() {
//        when(repo.findById(1L)).thenReturn(Optional.of(createdEntity));
//
//        Optional<TestRun> out = service.cancel(1L);
//
//        assertThat(out).isPresent();
//        assertThat(out.get().getStatus()).isEqualTo(TestStatus.CANCELLED);
//        assertThat(out.get().getFinishedAt()).isNotNull();
//
//        verify(repo, times(1)).findById(1L);
//        verifyNoMoreInteractions(repo);
//    }
//
//    @Test
//    void cancel_whenNotCreated_throws() {
//        TestRun r = new TestRun();
//        r.setId(7L);
//        r.setStatus(TestStatus.APPROVED);
//        when(repo.findById(7L)).thenReturn(Optional.of(r));
//
//        assertThrows(IllegalStateException.class, () -> service.cancel(7L));
//
//        verify(repo, times(1)).findById(7L);
//        verifyNoMoreInteractions(repo);
//    }
//}
