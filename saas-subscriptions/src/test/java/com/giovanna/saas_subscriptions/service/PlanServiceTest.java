package com.giovanna.saas_subscriptions.service;

import com.giovanna.saas_subscriptions.dto.PlanDto;
import com.giovanna.saas_subscriptions.enums.PlanType;
import com.giovanna.saas_subscriptions.exception.InvalidOperationException;
import com.giovanna.saas_subscriptions.exception.ResourceNotFoundException;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.repository.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {
    @Mock
    private PlanRepository repository;

    @InjectMocks
    private PlanService planService;

    private Plan plan;
    private PlanDto planDto;

    @BeforeEach
    void setUp() {
        planDto = new PlanDto(PlanType.SEMESTER, 99.90, "Premium", false);
        plan = new Plan("123", PlanType.SEMESTER, 99.90, "Premium", false);
    }

    @Test
    void save_Success() {
        when(repository.save(any(Plan.class))).thenReturn(plan);

        Plan result = planService.save(planDto);

        assertNotNull(result);

        assertEquals(planDto.type(), result.getType());
        assertEquals(planDto.price(), result.getPrice());
        assertEquals(planDto.name(), result.getName());
        assertEquals(planDto.active(), result.isActive());

        verify(repository, times(1)).save(any(Plan.class));
    }

    @Test
    void retrieve_Success() {
        when(repository.findById(plan.getId())).thenReturn(Optional.of(plan));

        Plan result = planService.retrieve(plan.getId());

        assertNotNull(result);

        assertEquals(plan.getId(), result.getId());
        assertEquals(planDto.type(), result.getType());
        assertEquals(planDto.price(), result.getPrice());
        assertEquals(planDto.name(), result.getName());
        assertEquals(planDto.active(), result.isActive());

        verify(repository, times(1)).findById(plan.getId());
    }

    @Test
    void retrieve_NotFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception =assertThrows(ResourceNotFoundException.class, () -> planService.retrieve("999"));
        assertEquals(exception.getMessage(), "plan with id 999 not found");

        verify(repository, times(1)).findById("999");
    }

    @Test
    void activate_Success() {
        plan.setActive(false);

        when(repository.findById(plan.getId())).thenReturn(Optional.of(plan));
        when(repository.save(any(Plan.class))).thenReturn(plan);

        Plan result = planService.activate(plan.getId());

        assertTrue(result.isActive());

        verify(repository, times(1)).save(any(Plan.class));
    }

    @Test
    void activate_AlreadyActive() {
        plan.setActive(true);
        when(repository.findById(plan.getId())).thenReturn(Optional.of(plan));

        assertThrows(InvalidOperationException.class, () -> planService.activate(plan.getId()));

        verify(repository, never()).save(any());
    }

    @Test
    void deactivate_Success() {
        plan.setActive(true);

        when(repository.findById(plan.getId())).thenReturn(Optional.of(plan));
        when(repository.save(any(Plan.class))).thenReturn(plan);

        Plan result = planService.deactivate(plan.getId());

        assertFalse(result.isActive());

        verify(repository, times(1)).save(any(Plan.class));
    }

    @Test
    void deactivate_AlreadyInactive() {
        plan.setActive(false);
        when(repository.findById(plan.getId())).thenReturn(Optional.of(plan));

        assertThrows(InvalidOperationException.class, () -> planService.deactivate(plan.getId()));

        verify(repository, never()).save(any());
    }
}
