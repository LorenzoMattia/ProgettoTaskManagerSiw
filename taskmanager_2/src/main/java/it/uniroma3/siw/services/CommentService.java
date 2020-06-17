package it.uniroma3.siw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	protected CommentRepository commentRepository;
	
	@Transactional
	public Comment save(Comment c) {
		return this.commentRepository.save(c);
	}
}
