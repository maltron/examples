//
//  Person.m
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "Person.h"


@implementation Person

@dynamic iD;
@dynamic lastName;
@dynamic firstName;

+(id)entityName {
    return NSStringFromClass(self);
}

+(NSEntityDescription *)entity:(NSManagedObjectContext *)context {
    NSLog(@"[Person entity] %@", [Person entityName]);
    return [NSEntityDescription entityForName:[Person entityName] inManagedObjectContext:context];
}

+(Person *)newPerson:(NSManagedObjectContext *)context {
    return (Person *)[[NSManagedObject alloc] initWithEntity:[Person entity:context] insertIntoManagedObjectContext:context];
}

+(NSData *)toJSON:(NSNumber *)iD firstName:(NSString *)firstName andLastName:(NSString *)lastName {
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    if(iD) [dict setObject:iD forKey:@"ID"];
    if(firstName) [dict setObject:firstName forKey:@"firstName"];
    if(lastName) [dict setObject:lastName forKey:@"lastName"];
    
    return [NSJSONSerialization dataWithJSONObject:dict options:kNilOptions error:nil];
}

-(NSData *)toJSON {
    NSLog(@"[PersonCRUD toJSON] #2");
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    if([self iD]) [dict setObject:[self iD] forKey:@"ID"];
    if([self firstName]) [dict setObject:[self firstName] forKey:@"firstName"];
    if([self lastName]) [dict setObject:[self lastName] forKey:@"lastName"];
    
    return [NSJSONSerialization dataWithJSONObject:dict options:kNilOptions error:nil];
}

-(void)parseFromJSON:(NSData *)data {
    NSLog(@"[PersonCRUD parseFromJSON] %@", data != nil ? [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding] : @"NIL");
    NSError *error = nil;
    id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    
    if(error) NSLog(@"### [Person parseFromJSON] Error:%@, %@", error, [error userInfo]);
    
    else if([json isKindOfClass:[NSDictionary class]]) {
        [self parseFromDictionary:(NSDictionary *)json];
        
    } else NSLog(@"### [Person parseFromJSON] Uncompatible format to parse");
}


-(void)parseFromDictionary:(NSDictionary *)dictionary {
    id value = [dictionary objectForKey:@"ID"];
    if(value) [self setID:value]; else [self setID:nil];
    
    value = [dictionary objectForKey:@"firstName"];
    if(value) [self setFirstName:value]; else [self setFirstName:nil];
    
    value = [dictionary objectForKey:@"lastName"];
    if(value) [self setLastName:value]; else [self setLastName:nil];
}

@end
