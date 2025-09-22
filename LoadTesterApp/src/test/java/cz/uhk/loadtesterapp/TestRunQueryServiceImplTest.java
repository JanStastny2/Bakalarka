//package cz.uhk.loadtesterapp;
//
//import cz.uhk.loadtesterapp.model.entity.TestRun;
//import cz.uhk.loadtesterapp.repository.TestRepository;
//import cz.uhk.loadtesterapp.service.TestRunQueryServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TestRunQueryServiceImplTest {
//
//    @Mock
//    private TestRepository testRepository;
//
//    @InjectMocks
//    private TestRunQueryServiceImpl service;
//
//    @Test
//    void findAll_returnsListFromRepository() {
//        // given
//        TestRun r1 = new TestRun();
//        r1.setId(1L);
//        TestRun r2 = new TestRun();
//        r2.setId(2L);
//        when(testRepository.findAll()).thenReturn(List.of(r1, r2));
//
//        // when
//        List<TestRun> result = service.list();
//
//        // then
//        assertThat(result).hasSize(2).containsExactly(r1, r2);
//        verify(testRepository, times(1)).findAll();
//        verifyNoMoreInteractions(testRepository);
//    }
//
//    @Test
//    void findById_whenPresent_returnsOptionalWithEntity() {
//        // given
//        TestRun run = new TestRun();
//        run.setId(42L);
//        when(testRepository.findById(42L)).thenReturn(Optional.of(run));
//
//        // when
//        Optional<TestRun> result = service.findById(42L);
//
//        // then
//        assertThat(result).isPresent().contains(run);
//        verify(testRepository, times(1)).findById(42L);
//        verifyNoMoreInteractions(testRepository);
//    }
//
//    @Test
//    void findById_whenMissing_returnsEmptyOptional() {
//        // given
//        when(testRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
//
//        // when
//        Optional<TestRun> result = service.findById(999L);
//
//        // then
//        assertThat(result).isEmpty();
//        verify(testRepository, times(1)).findById(999L);
//        verifyNoMoreInteractions(testRepository);
//    }
//}