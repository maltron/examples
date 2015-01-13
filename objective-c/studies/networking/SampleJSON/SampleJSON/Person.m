//
//  Person.m
//  SampleJSON
//
//  Created by Mauricio Leal on 1/13/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "Person.h"


@implementation Person

@dynamic firstName;
@dynamic lastName;

+(Person *)parseJSON:(NSData *)data {
    Person *person = [[Person alloc] init];
    
    NSError *error = nil;
    id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    if(error) {
        NSLog(@"### [Person parseJSON] Error:%@", error);
    } else {
        if([json isKindOfClass:[NSArray class]]) {
            NSLog(@"[Person parseJSON] JSON is NSArray");
            
        } else if([json isKindOfClass:[NSDictionary class]]) {
            NSLog(@"[Parse parseJSON] JSON is Dictionary");
            NSDictionary *dictionary = (NSDictionary *)json;
            [person setFirstName:[dictionary objectForKey:@"FirstName"]];
            [person setLastName:[dictionary objectForKey:@"LastName"]];
            
        }
    }
    
    return person;
}

@end
