package com.example.laptopstore.test;

import com.example.laptopstore.controller.LaptopController;
import com.example.laptopstore.dto.LaptopDTO;
import com.example.laptopstore.entity.Laptop;
import com.example.laptopstore.exception.ResourceNotFoundException;
import com.example.laptopstore.service.LaptopService;
import com.example.laptopstore.service.impl.LaptopServiceImpl;
import com.example.laptopstore.repo.LaptopRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaptopStoreTests {
    @Mock
    private LaptopService laptopService;
    @Mock
    private LaptopRepository laptopRepository;
    @InjectMocks
    private LaptopController laptopController;
    @InjectMocks
    private LaptopServiceImpl laptopServiceImpl;

    private LaptopDTO sampleLaptopDTO;
    private Laptop sampleLaptop;

    @BeforeEach
    void setUp() {
        sampleLaptopDTO = new LaptopDTO(1L, "Dell Inspiron", 800.0, "Dell", "512GB SSD", "16GB", "i7");
        sampleLaptop = new Laptop(1L, "Dell Inspiron", 800.0, "Dell", "512GB SSD", "16GB", "i7");
    }

    // Controller Tests
    @Test
    void testCreateLaptop() {
        when(laptopService.createLaptop(any(LaptopDTO.class))).thenReturn(sampleLaptopDTO);
        ResponseEntity<LaptopDTO> response = laptopController.createLaptop(sampleLaptopDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleLaptopDTO, response.getBody());
    }

    @Test
    void testGetLaptopById() {
        when(laptopService.getLaptopById(1L)).thenReturn(sampleLaptopDTO);
        ResponseEntity<LaptopDTO> response = laptopController.getLaptopById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleLaptopDTO, response.getBody());
    }

    @Test
    void testGetAllLaptops() {
        List<LaptopDTO> laptops = Arrays.asList(sampleLaptopDTO);
        when(laptopService.getAllLaptops()).thenReturn(laptops);
        ResponseEntity<List<LaptopDTO>> response = laptopController.getAllLaptops();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(laptops, response.getBody());
    }

    @Test
    void testUpdateLaptop() {
        when(laptopService.updateLaptop(eq(1L), any(LaptopDTO.class))).thenReturn(sampleLaptopDTO);
        ResponseEntity<LaptopDTO> response = laptopController.updateLaptop(1L, sampleLaptopDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleLaptopDTO, response.getBody());
    }

    @Test
    void testDeleteLaptop() {
        when(laptopService.deleteLaptop(1L)).thenReturn(true);
        ResponseEntity<Void> response = laptopController.deleteLaptop(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testSearchLaptops() {
        List<LaptopDTO> laptops = Arrays.asList(sampleLaptopDTO);
        when(laptopService.searchLaptops("Dell Inspiron", 800.0, "Dell")).thenReturn(laptops);
        ResponseEntity<List<LaptopDTO>> response = laptopController.searchLaptops("Dell Inspiron", 800.0, "Dell");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(laptops, response.getBody());
    }

    // ServiceImpl Tests
    @Test
    void testServiceGetAllLaptops() {
        when(laptopRepository.findAll()).thenReturn(Arrays.asList(sampleLaptop));
        List<LaptopDTO> result = laptopServiceImpl.getAllLaptops();
        assertEquals(1, result.size());
        assertEquals("Dell Inspiron", result.get(0).getName());
    }

    @Test
    void testServiceGetLaptopByIdFound() {
        when(laptopRepository.findById(1L)).thenReturn(Optional.of(sampleLaptop));
        LaptopDTO result = laptopServiceImpl.getLaptopById(1L);
        assertEquals("Dell Inspiron", result.getName());
    }

    @Test
    void testServiceGetLaptopByIdNotFound() {
        when(laptopRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> laptopServiceImpl.getLaptopById(2L));
    }

    @Test
    void testServiceCreateLaptop() {
        when(laptopRepository.save(any(Laptop.class))).thenReturn(sampleLaptop);
        LaptopDTO result = laptopServiceImpl.createLaptop(sampleLaptopDTO);
        assertEquals("Dell Inspiron", result.getName());
    }

    @Test
    void testServiceUpdateLaptopFound() {
        when(laptopRepository.findById(1L)).thenReturn(Optional.of(sampleLaptop));
        when(laptopRepository.save(any(Laptop.class))).thenReturn(sampleLaptop);
        LaptopDTO result = laptopServiceImpl.updateLaptop(1L, sampleLaptopDTO);
        assertEquals("Dell Inspiron", result.getName());
    }

    @Test
    void testServiceUpdateLaptopNotFound() {
        when(laptopRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> laptopServiceImpl.updateLaptop(2L, sampleLaptopDTO));
    }

    @Test
    void testServiceDeleteLaptopFound() {
        when(laptopRepository.existsById(1L)).thenReturn(true);
        doNothing().when(laptopRepository).deleteById(1L);
        assertTrue(laptopServiceImpl.deleteLaptop(1L));
    }

    @Test
    void testServiceDeleteLaptopNotFound() {
        when(laptopRepository.existsById(2L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> laptopServiceImpl.deleteLaptop(2L));
    }

    @Test
    void testServiceSearchLaptopsByName() {
        when(laptopRepository.findByName("Dell Inspiron")).thenReturn(Arrays.asList(sampleLaptop));
        List<LaptopDTO> result = laptopServiceImpl.searchLaptopsByName("Dell Inspiron");
        assertEquals(1, result.size());
        assertEquals("Dell Inspiron", result.get(0).getName());
    }

    @Test
    void testServiceSearchLaptopsByPrice() {
        when(laptopRepository.findByPrice(800.0)).thenReturn(Arrays.asList(sampleLaptop));
        List<LaptopDTO> result = laptopServiceImpl.searchLaptopsByPrice(800.0);
        assertEquals(1, result.size());
        assertEquals(800.0, result.get(0).getPrice());
    }

    @Test
    void testServiceSearchLaptopsByBrand() {
        when(laptopRepository.findByBrand("Dell")).thenReturn(Arrays.asList(sampleLaptop));
        List<LaptopDTO> result = laptopServiceImpl.searchLaptopsByBrand("Dell");
        assertEquals(1, result.size());
        assertEquals("Dell", result.get(0).getBrand());
    }

    @Test
    void testServiceSearchLaptops() {
        when(laptopRepository.findByNameOrPriceOrBrand("Dell Inspiron", 800.0, "Dell")).thenReturn(Arrays.asList(sampleLaptop));
        List<LaptopDTO> result = laptopServiceImpl.searchLaptops("Dell Inspiron", 800.0, "Dell");
        assertEquals(1, result.size());
        assertEquals("Dell Inspiron", result.get(0).getName());
    }
}
