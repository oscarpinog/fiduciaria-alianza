package com.alianza.fiduciaria.service;

import com.alianza.fiduciaria.DTO.ClientDTO;
import com.alianza.fiduciaria.model.ClientEntity;
import com.alianza.fiduciaria.respository.ClientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

	private final ClientRepository clientRepository;

	public ClientService(final ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public List<ClientDTO> getAllClients() {
		logger.info("Ejecutando metodo getAllClients()");
		List<ClientEntity> clientEntities = this.clientRepository.findAll();
		if (CollectionUtils.isEmpty(clientEntities)) {
			throw new RuntimeException("There are not clients yet.");
		}

		return clientEntities.stream()
				.map(clientEntity -> new ClientDTO(clientEntity.getID(), clientEntity.getSharedKey(),
						clientEntity.getBusinessId(), clientEntity.getEmail(), clientEntity.getPhone(),
						clientEntity.getDataAdded()))
				.collect(Collectors.toList());
	}

	public ClientDTO createStudent(final ClientDTO clientToCreate) {
		ClientEntity clientEntity = ClientEntity.builder()
				// .ID(clientToCreate.getID())
				.sharedKey(clientToCreate.getSharedKey()).businessId(clientToCreate.getBusinessId())
				.email(clientToCreate.getEmail()).phone(clientToCreate.getPhone())
				.dataAdded(clientToCreate.getDataAdded()).build();
		System.out.println("IDClienteCreate:" + clientToCreate.getID());

		this.clientRepository.save(clientEntity);

		logger.info("cliente creado exitosamente!");
		return clientToCreate;
	}

	public ClientDTO updateStudent(final ClientDTO clientToUpdate) {
		ClientEntity clientEntity = ClientEntity.builder().ID(clientToUpdate.getID())
				.sharedKey(clientToUpdate.getSharedKey()).businessId(clientToUpdate.getBusinessId())
				.email(clientToUpdate.getEmail()).phone(clientToUpdate.getPhone())
				.dataAdded(clientToUpdate.getDataAdded()).build();

		System.out.println("IDClienteUpdate:" + clientToUpdate.getID());

		this.clientRepository.save(clientEntity);
		logger.info("cliente actualizado exitosamente!");
		return clientToUpdate;

	}

	public ClientDTO deleteStudent(final String ID) {
		ClientEntity clientEntity = this.clientRepository.getReferenceById(ID);
		if (clientEntity == null) {
			throw new RuntimeException(String.format("Does not find the Client %s.", ID));
		}
		this.clientRepository.delete(clientEntity);

		logger.info("cliente Eliminado exitosamente!");

		return new ClientDTO(clientEntity.getID(), clientEntity.getSharedKey(), clientEntity.getBusinessId(),
				clientEntity.getEmail(), clientEntity.getPhone(), clientEntity.getDataAdded());
	}

	public ClientDTO getBySharedKey(String sharedKey) {

		ClientEntity clientEntity = this.clientRepository.findBySharedKey(sharedKey);
		if (clientEntity == null) {
			logger.info("cliente no encontrado sharedKey:" + sharedKey);
			throw new RuntimeException(String.format("Does not find the Client %s.", sharedKey));
		}

		logger.info("cliente encontrado exitosamente!");
		return new ClientDTO(clientEntity.getID(), clientEntity.getSharedKey(), clientEntity.getBusinessId(),
				clientEntity.getEmail(), clientEntity.getPhone(), clientEntity.getDataAdded());
	}
}
