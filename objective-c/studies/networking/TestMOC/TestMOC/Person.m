//
//  Person.m
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "Person.h"


@implementation Person

@dynamic iD;
@dynamic firstName;
@dynamic lastName;

+(NSEntityDescription *)entity:(NSManagedObjectContext *)context {
    return [NSEntityDescription entityForName:@"Person" inManagedObjectContext:context];
}

+(Person *)newPerson:(NSManagedObjectContext *)context {
    return (Person *)[[NSManagedObject alloc] initWithEntity:[Person entity:context] insertIntoManagedObjectContext:context];
}

-(void)parseJSON:(NSData *)data {
    NSError *error = nil;
    id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    
    if(error) {
        NSLog(@"### [Person parseJSON] Error:%@, %@", error, [error userInfo]);
    } else if([json isKindOfClass:[NSDictionary class]]) {
        [self parseJSONfromDictionary:(NSDictionary *)json];
    }
}

-(void)parseJSONfromDictionary:(NSDictionary *)dict {
    id value = [dict objectForKey:@"ID"];
    if(value) [self setID:value]; else [self setID:nil];
    
    value = [dict objectForKey:@"firstName"];
    if(value) [self setFirstName:value]; else [self setFirstName:nil];
    
    value = [dict objectForKey:@"lastName"];
    if(value) [self setLastName:value]; else [self setLastName:nil];
}

-(NSData *)toJSON {
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    if([self iD]) [dict setObject:[self iD] forKey:@"ID"];
    if([self firstName]) [dict setObject:[self firstName] forKey:@"firstName"];
    if([self lastName]) [dict setObject:[self lastName] forKey:@"lastName"];
    
    return [NSJSONSerialization dataWithJSONObject:dict options:kNilOptions error:nil];
}

@end
