/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/
package com.brightywe.brightylist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.repository.UserRepository;

/**
 * Class BrightySecurityUserDetailsService for mapping user entity and CustomUserDetails for web security.
 */
@Service
public class BrightySecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
            
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Name", username));
            if (user==null) {
                throw new UsernameNotFoundException("UserName "+ username +" not found");
            }
            return new CustomUserDetails(user);
    }
      
}
